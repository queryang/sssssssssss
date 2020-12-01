package sg.storage.web;

import com.alibaba.fastjson.JSONArray;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sg.storage.common.entity.CodeEnum;
import sg.storage.common.entity.ResultModel;
import sg.storage.common.util.ResultModelUtil;
import sg.storage.service.IFileService;
import sg.storage.model.vo.file.FileRequest;

@Slf4j
@CrossOrigin
@RestController
@Api(tags = "文件API")
@RequestMapping(value = "api/file")
public class FileController {

    @Autowired
    private IFileService fileService;

    /**
     * 删除文件（数据删除）
     *
     * @param ids 文件id
     * @return
     */
    @DeleteMapping(produces = "application/json;charset=UTF-8")
    public ResultModel deleteReal(@RequestBody JSONArray ids) {
        if (null == ids || 0 == ids.size()) {
            return ResultModelUtil.getFailInstance(CodeEnum.NO_PARAM, "未获取到文件id");
        }
        return fileService.deleteReal(ids);
    }

    /**
     * 删除文件（数据标记）
     *
     * @param ids 文件id（多个英文逗号分割）
     * @return
     */
    @DeleteMapping(value = "/logic", produces = "application/json;charset=UTF-8")
    public ResultModel deleteLogic(@RequestBody JSONArray ids) {
        if (null == ids || 0 == ids.size()) {
            return ResultModelUtil.getFailInstance(CodeEnum.NO_PARAM, "未获取到文件id");
        }
        return fileService.deleteLogic(ids);
    }

    /**
     * 复制文件
     *
     * @param fileRequest
     * @return
     */
    @PostMapping(value = "/replica", produces = "application/json;charset=UTF-8")
    public ResultModel copyFile(@RequestBody FileRequest fileRequest) {
        if (null == fileRequest) {
            return ResultModelUtil.getFailInstance(CodeEnum.NO_PARAM, "未获取到任何参数");
        }
        if (StringUtils.isBlank(fileRequest.getId())) {
            return ResultModelUtil.getFailInstance(CodeEnum.NO_PARAM, "未获取到文件id");
        }
        if (StringUtils.isBlank(fileRequest.getFolderId())) {
            return ResultModelUtil.getFailInstance(CodeEnum.NO_PARAM, "未获取到文件夹id");
        }
        return fileService.copyFile(fileRequest.getId(), fileRequest.getFolderId());
    }

    /**
     * 修改文件所属文件夹（移动文件）
     *
     * @param fileRequest
     * @return
     */
    @PutMapping(value = "/folder", produces = "application/json;charset=UTF-8")
    public ResultModel updateFolder(@RequestBody FileRequest fileRequest) {
        if (null == fileRequest) {
            return ResultModelUtil.getFailInstance(CodeEnum.NO_PARAM, "未获取到任何参数");
        }
        if (StringUtils.isBlank(fileRequest.getId())) {
            return ResultModelUtil.getFailInstance(CodeEnum.NO_PARAM, "未获取到文件id");
        }
        if (StringUtils.isBlank(fileRequest.getFolderId())) {
            return ResultModelUtil.getFailInstance(CodeEnum.NO_PARAM, "未获取到文件夹id");
        }
        return fileService.updateFileFolder(fileRequest.getId(), fileRequest.getFolderId());
    }

    /**
     * 按id查询转化文件
     *
     * @param id 文件id
     * @return
     */
    @GetMapping(produces = "application/json;charset=UTF-8")
    public ResultModel findTrans(@RequestParam String id) {
        if (StringUtils.isBlank(id)) {
            return ResultModelUtil.getFailInstance(CodeEnum.NO_PARAM, "未获取到文件id");
        }
        return fileService.findTrans(id);
    }

    /**
     * 按id查询转化文件（Restful）
     *
     * @param id 文件id
     * @return
     */
    @GetMapping(value = "/{id}", produces = "application/json;charset=UTF-8")
    public ResultModel restFindTrans(@PathVariable("id") String id) {
        return findTrans(id);
    }

    /**
     * 按文件夹id查询文件
     *
     * @param folderId 文件夹id
     * @return
     */
    @GetMapping(value = "/folder", produces = "application/json;charset=UTF-8")
    public ResultModel findByFolderId(@RequestParam String folderId) {
        if (StringUtils.isBlank(folderId)) {
            return ResultModelUtil.getFailInstance(CodeEnum.NO_PARAM, "未获取到文件夹id");
        }
        return fileService.findByFolderId(folderId);
    }

    /**
     * 按文件夹id查询文件（Restful）
     *
     * @param folderId 文件夹id
     * @return
     */
    @GetMapping(value = "/folder/{folderId}", produces = "application/json;charset=UTF-8")
    public ResultModel restFindByFolderId(@PathVariable("folderId") String folderId) {
        return findByFolderId(folderId);
    }

    /**
     * 按文件夹id分页查询文件
     *
     * @param folderId 文件夹id
     * @param pageNum  当前页码
     * @param pageSize 每页条数
     * @return
     */
    @GetMapping(value = "/folder-page/{pageNum}/{pageSize}", produces = "application/json;charset=UTF-8")
    public ResultModel findPageByFolderId(@RequestParam String folderId
            , @PathVariable("pageNum") Integer pageNum
            , @PathVariable("pageSize") Integer pageSize) {
        if (StringUtils.isBlank(folderId)) {
            return ResultModelUtil.getFailInstance(CodeEnum.NO_PARAM, "未获取到文件夹id");
        }
        if (pageNum < 0) {
            return ResultModelUtil.getFailInstance(CodeEnum.PARAM_ERROR, "当前页码参数错误");
        }
        if (pageSize <= 0) {
            return ResultModelUtil.getFailInstance(CodeEnum.PARAM_ERROR, "每页条数参数错误");
        }
        return fileService.findPageByFolderId(folderId, pageNum, pageSize);
    }

    /**
     * 删除文件夹下文件
     *
     * @param folderId 文件夹id
     * @return
     */
    @DeleteMapping(value = "/folder-file", produces = "application/json;charset=UTF-8")
    public ResultModel deleteFolderFile(@RequestBody String folderId) {
        if (StringUtils.isBlank(folderId)) {
            return ResultModelUtil.getFailInstance(CodeEnum.NO_PARAM, "未获取到文件夹id");
        }
        return fileService.deleteFolderFile(folderId);
    }

    /**
     * 删除文件夹下文件（Restful）
     *
     * @param folderId 文件夹id
     * @return
     */
    @DeleteMapping(value = "/folder-file/{folderId}", produces = "application/json;charset=UTF-8")
    public ResultModel restDeleteFolderFile(@PathVariable("folderId") String folderId) {
        return deleteFolderFile(folderId);
    }
}