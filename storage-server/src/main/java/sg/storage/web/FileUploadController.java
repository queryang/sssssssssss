package sg.storage.web;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
import java.util.*;

@Slf4j
@CrossOrigin
@RestController
@Api(tags = "文件上传API")
@RequestMapping(value = "/api/file")
public class FileUploadController {

    @Autowired
    private FileProperties fileProperties;
    @Autowired
    @Qualifier(value = "localUploadService")
    private IFileUploadService localUploadService;
    @Autowired
    @Qualifier(value = "ossUploadService")
    private IFileUploadService ossUploadService;

//    public static final String SCS_URL = "http://192.168.3.128/scs/fault/storage";
//    public static final String SCS_URL_LOCAL = "http://localhost/scs/fault/storage";
    public static final String SCS_PATH = "H:\\scs\\";
//    @ApiOperation(value = "上传文件", notes = "上传文件")
//    @PostMapping(produces = "application/json;charset=UTF-8")
//    public ResultModel<FileUploadResponse> upload(
//            @ApiParam(value = "appKey", required = true) String appKey
//            , @ApiParam(value = "ownId", required = true) String ownId
//            , @ApiParam(value = "备注") String remark
//            , @ApiParam(value = "文件夹id") String folderId
//            , @ApiParam(value = "上传人") String uploadUser
//            , HttpServletRequest request) {
//        if (StringUtils.isBlank(appKey)) {
//            return ResultModelUtil.getFailInstance(CodeEnum.NO_PARAM, "未获取到appKey");
//        }
//        if (StringUtils.isBlank(ownId)) {
//            return ResultModelUtil.getFailInstance(CodeEnum.NO_PARAM, "未获取到ownId");
//        }
//        FileRequest fileRequest = new FileRequest();
//        fileRequest.setAppKey(appKey);
//        fileRequest.setOwnId(ownId);
//        fileRequest.setRemark(remark);
//        fileRequest.setFolderId(folderId);
//        fileRequest.setUploadUser(uploadUser);
//        ResultModel<FileUploadResponse> resultModel = fileReceive(fileRequest, request);
//        List<FileResponse> successFiles = resultModel.getData().getSuccessFiles();
//        //存储文件信息容器
//        Map<String,String> map = new HashMap<>();
//        //获取文件信息
//        for (FileResponse successFile : successFiles) {
//            String filePath = successFile.getFilePath();
//            map.put(successFile.getOriginalFileName(),filePath);
//        }
//
//        JSONObject jsonObject = (JSONObject)JSONObject.toJSON(map);
//        String s = HttpUtils.sendPostJson(SCS_URL_LOCAL, jsonObject);
//        System.out.println(s);
//        return resultModel;
//    }


    @ApiOperation(value = "修改后上传文件", notes = "修改后上传文件")
    @PostMapping(produces = "application/json;charset=UTF-8")
    public ResultModel<FileUploadResponse> uploadStorage(HttpServletRequest request, @ApiParam(value = "type",required = true)String type) {

        FileRequest fileRequest = new FileRequest();
        //加入预存路径
        fileRequest.setPath(SCS_PATH+type);
        ResultModel<FileUploadResponse> resultModel = fileReceive(fileRequest, request);

        return resultModel;
    }


    @ApiOperation(value = "更换文件", notes = "更换文件")
    @PostMapping(value = "/{id}", produces = "application/json;charset=UTF-8")
    public ResultModel<FileUploadResponse> update(
            @PathVariable(value = "id") String id
            , @ApiParam(value = "备注") String remark
            , @ApiParam(value = "上传人") String uploadUser
            , HttpServletRequest request) {
        if (StringUtils.isBlank(id)) {
            return ResultModelUtil.getFailInstance(CodeEnum.NO_PARAM, "未获取到文件id");
        }
        FileRequest fileRequest = new FileRequest();
        fileRequest.setId(id);
        fileRequest.setRemark(remark);
        fileRequest.setUploadUser(uploadUser);
        return fileReceive(fileRequest, request);
    }

    /**
     * 文件接收
     *
     * @param fileRequest
     * @param request
     * @return
     */
    private ResultModel<FileUploadResponse> fileReceive(FileRequest fileRequest, HttpServletRequest request) {
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
                    //本地存储信息
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

//    /**
//     * 文件接收
//     *
//     * @param fileRequest
//     * @param request
//     * @return
//     */
//    @RequestMapping("/bzxx")
//    private ResultModel<FileUploadResponse> fileReceiveBzxx(FileRequest fileRequest, HttpServletRequest request) {
//        List<FileByte> fileBytes = getFileByte(fileRequest, request);
//        if (CollectionUtils.isEmpty(fileBytes)) {
//            return ResultModelUtil.getFailInstance(CodeEnum.NO_PARAM, "未获取到文件");
//        }
//        /**
//         * 若文件id为空，则为新增文件
//         * 若文件id不为空，则为更新文件，更新文件仅可一份文件
//         */
//        if (StringUtils.isBlank(fileRequest.getId())) {
//            switch (fileProperties.getLocation()) {
//                case FileConstant.LOCAL:
//                    //本地存储
//                    return localUploadService.receiveAndAdd(fileBytes);
//                case FileConstant.OSS:
//                    return ossUploadService.receiveAndAdd(fileBytes);
//                default:
//                    return ResultModelUtil.getFailInstance(CodeEnum.NO_PARAM, "未获取到存储方式");
//            }
//        }
//        FileByte fileByte = fileBytes.get(0);
//        fileByte.setId(fileRequest.getId());
//        switch (fileProperties.getLocation()) {
//            case FileConstant.LOCAL:
//                return localUploadService.receiveAndUpdate(fileByte);
//            case FileConstant.OSS:
//                return ossUploadService.receiveAndUpdate(fileByte);
//            default:
//                return ResultModelUtil.getFailInstance(CodeEnum.NO_PARAM, "未获取到存储方式");
//        }
//    }

    //        //todo 以下是通信主服务的请求
//        List<FileResponse> successFiles = resultModel.getData().getSuccessFiles();
//        //存储文件信息容器
//        Map<String,String> map = new HashMap<>();
//        //获取文件信息
//        for (FileResponse successFile : successFiles) {
//            String filePath = successFile.getFilePath();
//            map.put(successFile.getOriginalFileName(),filePath);
//        }
//
//        JSONObject jsonObject = (JSONObject)JSONObject.toJSON(map);
//        String s = HttpUtils.sendPostJson(SCS_URL_LOCAL, jsonObject);
//        System.out.println(s);


    /**
     * 获取组装文件流
     *
     * @param fileRequest
     * @param request
     * @return
     */
    private List<FileByte> getFileByte(FileRequest fileRequest, HttpServletRequest request) {
        List<FileByte> fileBytes = new ArrayList<>(1);
        //todo这里做了修改
        MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
        //天马行空的解决办法--暂时抛弃
//        MultipartResolver resolver = new CommonsMultipartResolver(request.getSession().getServletContext());
//        MultipartHttpServletRequest multipartHttpServletRequest = resolver.resolveMultipart(request);
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
                        //添加预存路径
                        .path(fileRequest.getPath())
                        .build();
            } catch (Exception e) {
                e.printStackTrace();
                log.error("获取组装文件流异常: {}", e.getMessage());
            }
            fileBytes.add(fileByte);
        }
        return fileBytes;
    }
}