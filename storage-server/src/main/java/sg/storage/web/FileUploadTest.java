package sg.storage.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import sg.storage.common.entity.CodeEnum;
import sg.storage.common.entity.ResultModel;
import sg.storage.common.properties.FileConstant;
import sg.storage.common.properties.FileProperties;
import sg.storage.common.util.EtagUtil;
import sg.storage.common.util.ResultModelUtil;
import sg.storage.model.dto.FileByte;
import sg.storage.model.vo.file.FileRequest;
import sg.storage.model.vo.file.FileUploadResponse;
import sg.storage.service.IFileUploadService;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class FileUploadTest {

    @Autowired
    private FileProperties fileProperties;
    @Autowired
    @Qualifier(value = "localUploadService")
    private IFileUploadService localUploadService;
    @Autowired
    @Qualifier(value = "ossUploadService")
    private IFileUploadService ossUploadService;


    /**
     * 文件接收
     *
     * @param fileRequest
     * @param request
     * @return
     */
    public ResultModel<FileUploadResponse> fileReceive(FileRequest fileRequest, HttpServletRequest request) {
        List<FileByte> fileBytes = getFileByte(fileRequest, request);
        if (CollectionUtils.isEmpty(fileBytes)) {
            return ResultModelUtil.getFailInstance(CodeEnum.NO_PARAM, "未获取到文件");
        }
        /**
         * 若文件id为空，则为新增文件
         * 若文件id不为空，则为更新文件，更新文件仅可一份文件
         */
        if (StringUtils.isBlank(fileRequest.getId())) {
            switch (fileProperties.getLocation()) {
                case FileConstant.LOCAL:
                    return localUploadService.receiveAndAdd(fileBytes);
                case FileConstant.OSS:
                    return ossUploadService.receiveAndAdd(fileBytes);
                default:
                    return ResultModelUtil.getFailInstance(CodeEnum.NO_PARAM, "未获取到存储方式");
            }
        }
        FileByte fileByte = fileBytes.get(0);
        fileByte.setId(fileRequest.getId());
        switch (fileProperties.getLocation()) {
            case FileConstant.LOCAL:
                return localUploadService.receiveAndUpdate(fileByte);
            case FileConstant.OSS:
                return ossUploadService.receiveAndUpdate(fileByte);
            default:
                return ResultModelUtil.getFailInstance(CodeEnum.NO_PARAM, "未获取到存储方式");
        }
    }

    /**
     * 获取组装文件流
     *
     * @param fileRequest
     * @param request
     * @return
     */
    public List<FileByte> getFileByte(FileRequest fileRequest, HttpServletRequest request) {
        List<FileByte> fileBytes = new ArrayList<>(1);
        MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
        EtagUtil etagUtil = new EtagUtil();
        Iterator<String> fileNames = multipartHttpServletRequest.getFileNames();
        while (fileNames.hasNext()) {
            MultipartFile multipartFile = multipartHttpServletRequest.getFile(fileNames.next());
            if (null == multipartFile) {
                continue;
            }
            FileByte fileByte = null;
            try {
                fileByte = FileByte.builder()
                        .appKey(fileRequest.getAppKey())
                        .ownId(fileRequest.getOwnId())
                        .etag(etagUtil.calcEtag(multipartFile.getInputStream(), multipartFile.getSize()))
                        .fileName(multipartFile.getOriginalFilename())
                        .fileSize(multipartFile.getSize())
                        .remark(fileRequest.getRemark())
                        .folderId(fileRequest.getFolderId())
                        .uploadUser(fileRequest.getUploadUser())
                        .contentType(multipartFile.getContentType())
                        .fileByte(multipartFile.getBytes())
                        .build();
            } catch (Exception e) {
                e.printStackTrace();
//                log.error("获取组装文件流异常: {}", e.getMessage());
            }
            fileBytes.add(fileByte);
        }
        return fileBytes;
    }
}