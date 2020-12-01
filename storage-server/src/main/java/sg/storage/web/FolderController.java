package sg.storage.web;

import com.alibaba.fastjson.JSONArray;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import sg.storage.common.entity.CodeEnum;
import sg.storage.common.entity.ResultModel;
import sg.storage.common.util.ResultModelUtil;
import sg.storage.model.vo.folder.FolderResponse;
import sg.storage.service.IFolderService;
import sg.storage.model.vo.folder.FolderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@CrossOrigin
@RestController
@Api(tags = "文件夹API")
@RequestMapping(value = "api/folder")
public class FolderController {

    @Autowired
    private IFolderService folderService;

    /**
     * 新增文件夹
     *
     * @param folderRequest
     * @return
     */
    @ApiOperation(value = "新增文件夹", notes = "新增文件夹")
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {"application/json;charset=UTF-8"})
    public ResultModel<FolderResponse> insert(@RequestBody FolderRequest folderRequest) {
        if (null == folderRequest) {
            return ResultModelUtil.getFailInstance(CodeEnum.NO_PARAM, "未获取到任何参数");
        }
        folderRequest.setId(null);
        if (StringUtils.isBlank(folderRequest.getAppKey())) {
            return ResultModelUtil.getFailInstance(CodeEnum.NO_PARAM, "未获取到appKey");
        }
        if (StringUtils.isBlank(folderRequest.getOwnId())) {
            return ResultModelUtil.getFailInstance(CodeEnum.NO_PARAM, "未获取到ownId");
        }
        if (null == folderRequest.getPid()) {
            return ResultModelUtil.getFailInstance(CodeEnum.NO_PARAM, "未获取到父文件夹id");
        }
        if (StringUtils.isBlank(folderRequest.getName())) {
            return ResultModelUtil.getFailInstance(CodeEnum.NO_PARAM, "未获取到文件夹名称");
        }
        if (null == folderRequest.getBeShare()) {
            return ResultModelUtil.getFailInstance(CodeEnum.NO_PARAM, "未获取到是否共享");
        }
        if (null == folderRequest.getBeReadOnly()) {
            return ResultModelUtil.getFailInstance(CodeEnum.NO_PARAM, "未获取到是否只读");
        }
        return folderService.insert(folderRequest);
    }

    /**
     * 修改文件夹
     *
     * @param folderRequest
     * @return
     */
    @ApiOperation(value = "修改文件夹", notes = "修改文件夹")
    @PutMapping(consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {"application/json;charset=UTF-8"})
    public ResultModel<FolderResponse> update(@RequestBody FolderRequest folderRequest) {
        if (null == folderRequest) {
            return ResultModelUtil.getFailInstance(CodeEnum.NO_PARAM, "未获取到任何参数");
        }
        if (StringUtils.isBlank(folderRequest.getId())) {
            return ResultModelUtil.getFailInstance(CodeEnum.NO_PARAM, "未获取到文件夹id");
        }
        if (StringUtils.isBlank(folderRequest.getName())) {
            return ResultModelUtil.getFailInstance(CodeEnum.NO_PARAM, "未获取到文件夹名称");
        }
        return folderService.update(folderRequest);
    }

    /**
     * 修改父文件夹id
     *
     * @param folderRequest
     * @return
     */
    @ApiOperation(value = "修改父文件夹id", notes = "修改父文件夹id")
    @PutMapping(value = "/pid", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {"application/json;charset=UTF-8"})
    public ResultModel<FolderResponse> updatePid(@RequestBody FolderRequest folderRequest) {
        if (null == folderRequest) {
            return ResultModelUtil.getFailInstance(CodeEnum.NO_PARAM, "未获取到任何参数");
        }
        if (StringUtils.isBlank(folderRequest.getId())) {
            return ResultModelUtil.getFailInstance(CodeEnum.NO_PARAM, "未获取到文件夹id");
        }
        if (StringUtils.isBlank(folderRequest.getPid())) {
            return ResultModelUtil.getFailInstance(CodeEnum.NO_PARAM, "未获取到父文件夹id");
        }
        return folderService.updatePid(folderRequest);
    }

    /**
     * 修改文件夹共享状态
     *
     * @param folderRequest
     * @return
     */
    @ApiOperation(value = "修改文件夹共享状态", notes = "修改文件夹共享状态")
    @PutMapping(value = "/share", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {"application/json;charset=UTF-8"})
    public ResultModel<FolderResponse> updateShare(@RequestBody FolderRequest folderRequest) {
        if (null == folderRequest) {
            return ResultModelUtil.getFailInstance(CodeEnum.NO_PARAM, "未获取到任何参数");
        }
        if (StringUtils.isBlank(folderRequest.getId())) {
            return ResultModelUtil.getFailInstance(CodeEnum.NO_PARAM, "未获取到文件夹id");
        }
        if (null == folderRequest.getBeShare()) {
            return ResultModelUtil.getFailInstance(CodeEnum.NO_PARAM, "未获取到是否共享标识");
        }
        if (0 != folderRequest.getBeShare() && 1 != folderRequest.getBeShare()) {
            return ResultModelUtil.getFailInstance(CodeEnum.PARAM_ERROR, "是否共享标识无效");
        }
        return folderService.updateShare(folderRequest);
    }

    /**
     * 修改文件夹只读状态
     *
     * @param folderRequest
     * @return
     */
    @ApiOperation(value = "修改文件夹只读状态", notes = "修改文件夹只读状态")
    @PutMapping(value = "/readonly", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {"application/json;charset=UTF-8"})
    public ResultModel<FolderResponse> updateReadOnly(@RequestBody FolderRequest folderRequest) {
        if (null == folderRequest) {
            return ResultModelUtil.getFailInstance(CodeEnum.NO_PARAM, "未获取到任何参数");
        }
        if (StringUtils.isBlank(folderRequest.getId())) {
            return ResultModelUtil.getFailInstance(CodeEnum.NO_PARAM, "未获取到文件夹id");
        }
        if (null == folderRequest.getBeReadOnly()) {
            return ResultModelUtil.getFailInstance(CodeEnum.NO_PARAM, "未获取到是否只读标识");
        }
        if (0 != folderRequest.getBeReadOnly() && 1 != folderRequest.getBeReadOnly()) {
            return ResultModelUtil.getFailInstance(CodeEnum.PARAM_ERROR, "是否只读标识无效");
        }
        return folderService.updateReadOnly(folderRequest);
    }

    /**
     * 删除文件夹（逻辑删除）
     *
     * @param ids 文件夹id集合
     * @return
     */
    @ApiOperation(value = "删除文件夹（逻辑删除）", notes = "入参: [1,2,3]")
    @DeleteMapping(value = "/fake", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {"application/json;charset=UTF-8"})
    public ResultModel<FolderResponse> deleteFake(@RequestBody JSONArray ids) {
        if (null == ids || 0 == ids.size()) {
            return ResultModelUtil.getFailInstance(CodeEnum.NO_PARAM, "未获取到文件夹id");
        }
        return folderService.deleteFake(ids);
    }

    /**
     * 删除文件夹（物理删除）
     *
     * @param ids 文件夹id集合
     * @return
     */
    @ApiOperation(value = "删除文件夹（物理删除）", notes = "入参: [1,2,3]")
    @DeleteMapping(value = "/real", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {"application/json;charset=UTF-8"})
    public ResultModel<FolderResponse> deleteReal(@RequestBody JSONArray ids) {
        if (null == ids || 0 == ids.size()) {
            return ResultModelUtil.getFailInstance(CodeEnum.NO_PARAM, "未获取到文件夹id");
        }
        return folderService.deleteReal(ids);
    }

    /**
     * 查询文件夹
     *
     * @param appKey      appKey
     * @param ownId       ownId
     * @param pid         父文件夹id
     * @param isFindAll   是否查询全部层级（0否1是）
     * @param isBuildTree 是否组装树结构（0否1是）
     * @return
     */
    @ApiOperation(value = "查询文件夹", notes = "查询文件夹")
    @GetMapping(produces = "application/json;charset=UTF-8")
    public ResultModel<List<FolderResponse>> findFolder(
            @ApiParam(value = "appKey", required = true, defaultValue = "1") @RequestParam String appKey
            , @ApiParam(value = "ownId", required = true, defaultValue = "1") @RequestParam String ownId
            , @ApiParam(value = "父文件夹id", required = true, defaultValue = "-1") @RequestParam String pid
            , @ApiParam(value = "是否查询全部层级（0否1是）", required = true, defaultValue = "0") @RequestParam Integer isFindAll
            , @ApiParam(value = "是否组装树结构（0否1是）", required = true, defaultValue = "0") @RequestParam Integer isBuildTree) {
        if (StringUtils.isBlank(appKey)) {
            return ResultModelUtil.getFailInstance(CodeEnum.NO_PARAM, "未获取到appKey");
        }
        if (StringUtils.isBlank(ownId)) {
            return ResultModelUtil.getFailInstance(CodeEnum.NO_PARAM, "未获取到ownId");
        }
        if (StringUtils.isBlank(pid)) {
            return ResultModelUtil.getFailInstance(CodeEnum.NO_PARAM, "未获取到父文件夹id");
        }
        if (null == isFindAll) {
            return ResultModelUtil.getFailInstance(CodeEnum.NO_PARAM, "未获取到是否查询全部层级");
        }
        if (null == isBuildTree) {
            return ResultModelUtil.getFailInstance(CodeEnum.NO_PARAM, "未获取到是否组装树结构");
        }
        if (0 != isFindAll && 1 != isFindAll) {
            return ResultModelUtil.getFailInstance(CodeEnum.PARAM_ERROR, "是否查询全部层级无效");
        }
        if (0 != isBuildTree && 1 != isBuildTree) {
            return ResultModelUtil.getFailInstance(CodeEnum.PARAM_ERROR, "是否组装树结构无效");
        }
        return folderService.findFolder(appKey, ownId, pid, isFindAll, isBuildTree);
    }

    /**
     * 查询共享文件夹
     *
     * @param appKey      appKey
     * @param pid         父文件夹id（非必填）
     * @param isFindAll   是否查询全部层级（0否1是）
     * @param isBuildTree 是否组装树结构（0否1是）
     * @return
     */
    @ApiOperation(value = "查询共享文件夹", notes = "查询共享文件夹")
    @GetMapping(value = "/share", produces = "application/json;charset=UTF-8")
    public ResultModel<List<FolderResponse>> findShareFolder(
            @ApiParam(value = "appKey", required = true, defaultValue = "1") @RequestParam String appKey
            , @ApiParam(value = "父文件夹id（非必填）", defaultValue = "-1") @RequestParam(required = false) String pid
            , @ApiParam(value = "是否查询全部层级（0否1是）", required = true, defaultValue = "0") @RequestParam Integer isFindAll
            , @ApiParam(value = "是否组装树结构（0否1是）", required = true, defaultValue = "0") @RequestParam Integer isBuildTree) {
        if (StringUtils.isBlank(appKey)) {
            return ResultModelUtil.getFailInstance(CodeEnum.NO_PARAM, "未获取到appKey");
        }
        if (null == isFindAll) {
            return ResultModelUtil.getFailInstance(CodeEnum.NO_PARAM, "未获取到是否查询全部层级");
        }
        if (null == isBuildTree) {
            return ResultModelUtil.getFailInstance(CodeEnum.NO_PARAM, "未获取到是否组装树结构");
        }
        if (0 != isFindAll && 1 != isFindAll) {
            return ResultModelUtil.getFailInstance(CodeEnum.PARAM_ERROR, "是否查询全部层级无效");
        }
        if (0 != isBuildTree && 1 != isBuildTree) {
            return ResultModelUtil.getFailInstance(CodeEnum.PARAM_ERROR, "是否组装树结构无效");
        }
        return folderService.findShareFolder(appKey, pid, isFindAll, isBuildTree);
    }
}