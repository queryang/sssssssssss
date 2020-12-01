package sg.storage.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sg.storage.common.entity.CodeEnum;
import sg.storage.common.entity.ResultModel;
import sg.storage.common.util.ResultModelUtil;
import sg.storage.common.properties.FileConstant;
import sg.storage.service.IFileDownloadService;
import sg.storage.service.IFileService;
import sg.storage.model.po.FileInfo;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@CrossOrigin
@Controller
@Api(tags = "文件下载API")
@RequestMapping(value = "api/file")
public class FileDownloadController {

    @Autowired
    private IFileService fileManageService;
    @Autowired
    @Qualifier(value = "localFileDownloadService")
    private IFileDownloadService localFileDownload;

    private static final String CONTENT_DISPOSITION = "Content-Disposition";
    private static final String DISPOSITION_FILENAME_NAME = "attachment; filename=";
    private static final String TARGET_CODE = "ISO-8859-1";

    /**
     * 下载文件（预览文件）
     *
     * @param id           原文件id（下载原文件以下属性忽略）
     * @param fileType     文件类型（原文件类型/png/pdf/html）
     * @param fileSizeType 尺寸（FileSizeTypeConf）
     */
    @GetMapping(value = "/file")
    public void download(@RequestParam String id
            , @RequestParam String fileType, @RequestParam String fileSizeType, HttpServletResponse response) {
        /**
         * 获取文件信息
         */
        FileInfo fileInfo = getFileInfo(id, fileType, fileSizeType);
        /**
         * 下载文件
         */
        download(fileInfo, response);
    }

    /**
     * 下载文件（预览文件）（Restful）
     *
     * @param id 原文件id
     */
    @GetMapping(value = "/file/{id}")
    public void RestDownload(@PathVariable("id") String id, HttpServletResponse response) {
        download(id, null, null, response);
    }

    /**
     * 下载文件（预览文件）（Restful）
     *
     * @param id           原文件id（下载原文件以下属性忽略）
     * @param fileType     文件类型（原文件类型/PNG/PDF/HTML）
     * @param fileSizeType 尺寸（FileSizeTypeConf）
     */
    @GetMapping(value = "/file/{id}/{fileType}/{fileSizeType}")
    public void RestDownload(@PathVariable("id") String id
            , @PathVariable("fileType") String fileType
            , @PathVariable("fileSizeType") String fileSizeType
            , HttpServletResponse response) {
        download(id, fileType, fileSizeType, response);
    }

    /**
     * ZIP批量下载文件
     *
     * @param ids 文件id（多个英文逗号拼接）
     */
    @GetMapping(value = "/zip")
    public void downloadZip(@RequestParam String ids, HttpServletResponse response) {
        List<String> filePaths = getFilePath(ids);
        OutputStream outputStream;
        try {
            outputStream = response.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        try {
            localFileDownload.zipWirte(outputStream, filePaths);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("ZIP批量下载失败:{}", e.getMessage());
        }
    }

    /**
     * ZIP批量下载文件（Restful）
     *
     * @param ids 文件id（多个英文逗号拼接）
     */
    @GetMapping(value = "/zip/{ids}")
    public void RestDownloadZip(@PathVariable("ids") String ids, HttpServletResponse response) {
        downloadZip(ids, response);
    }

    /**
     * 获取文件路径
     *
     * @param fileIds
     * @return
     */
    private List<String> getFilePath(String fileIds) {
        List<String> filePaths = new ArrayList<>(1);
        String[] fileIdArr = StringUtils.split(fileIds, ',');
        FileInfo fileInfo;
        for (int i = 0; i < fileIdArr.length; i++) {
            /**
             * 获取文件信息
             */
            fileInfo = getFileInfo(fileIdArr[i], null, null);
            filePaths.add(fileInfo.getFilePath());
        }
        return filePaths;
    }

    /**
     * 获取文件信息
     *
     * @param fileId
     * @param fileType
     * @param fileSizeType
     * @return
     */
    private FileInfo getFileInfo(String fileId, String fileType, String fileSizeType) {
        /**
         * 按文件id查询原文件信息
         */
        FileInfo originalFileInfo = fileManageService.findById(fileId);
        if (null == originalFileInfo) {
            return null;
        }
        if (StringUtils.isBlank(fileType)) {
            fileType = originalFileInfo.getFileSuffix();
        }
        if (StringUtils.isBlank(fileSizeType)) {
            fileSizeType = FileConstant.ORIGINAL;
        }
        /**
         * 按唯一标识、文件后缀、文件尺寸类型查询文件
         */
        List<FileInfo> fileInfos = localFileDownload.findByETagAndFileSuffixAndFileSizeType(originalFileInfo.getEtag()
                , fileType, fileSizeType);
        if (CollectionUtils.isNotEmpty(fileInfos)) {
            return fileInfos.get(0);
        }
        return null;
    }

    /**
     * 下载文件
     *
     * @param fileInfo
     * @param response
     */
    private void download(FileInfo fileInfo, HttpServletResponse response) {
        if (fileInfo != null) {
            java.io.File file = new java.io.File(fileInfo.getFilePath());
            if (file.exists()) {
                String filename = fileInfo.getOriginalFileName() + "." + fileInfo.getFileSuffix();
                try {
                    response.setHeader(CONTENT_DISPOSITION, DISPOSITION_FILENAME_NAME + new String(filename.getBytes(), TARGET_CODE));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    log.error("不支持当前编码");
                    return;
                }
                try (OutputStream out = response.getOutputStream()) {
                    localFileDownload.wirte(out, fileInfo.getFilePath());
                } catch (FileNotFoundException e) {
                    fileDownloadError(response, CodeEnum.ERROR);
                } catch (IOException e) {
                    log.error(e.getMessage());
                    fileDownloadError(response, CodeEnum.ERROR);
                }
            }
        }
    }

    /**
     * 文件下载失败
     *
     * @param response
     * @param codeEnum
     */
    private static void fileDownloadError(HttpServletResponse response, CodeEnum codeEnum) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            response.setHeader("Content-type", "application/Json;charset=UTF-8");
            ResultModel result = ResultModelUtil.getFailInstance(codeEnum, codeEnum.getMessage());
            mapper.writeValue(response.getWriter(), result);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}