package sg.storage.service.impl;

import com.alibaba.fastjson.JSONArray;
import org.apache.commons.lang3.StringUtils;
import sg.storage.common.entity.CodeEnum;
import sg.storage.common.entity.ResultModel;
import sg.storage.common.util.CommonUtil;
import sg.storage.common.util.ResultModelUtil;
import sg.storage.common.properties.FolderTypeProperties;
import sg.storage.service.IFolderService;
import sg.storage.common.ModelCover;
import sg.storage.model.vo.folder.FolderRequest;
import sg.storage.model.vo.folder.FolderResponse;
import sg.storage.model.po.FolderInfo;
import sg.storage.repository.FolderRepository;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FolderServiceImpl implements IFolderService {

    @Autowired
    private FolderTypeProperties folderTypeProperties;
    @Autowired
    private FolderRepository folderRepository;

    @Override
    public ResultModel<FolderResponse> insert(FolderRequest folderRequest) {
        FolderInfo folderInfo = new FolderInfo();
        ModelCover.folderRequestToPO(folderRequest, folderInfo);
        /**
         * 若pid不为-1，则查询父文件夹并同步共享状态与只读状态
         */
        if (-1 != folderInfo.getPid()) {
            FolderInfo parentFolder = findParentFolder(folderInfo.getPid());
            folderInfo.setBeShare(parentFolder.getBeShare());
            folderInfo.setBeReadOnly(parentFolder.getBeReadOnly());
        }
        /**
         * 新增文件夹
         */
        folderInfo = folderRepository.save(folderInfo);
        if (null == folderInfo) {
            return ResultModelUtil.getFailInstance(CodeEnum.ERROR, "新增文件夹失败");
        }
        FolderResponse folderResponse = new FolderResponse();
        ModelCover.folderPOToResponse(folderInfo, folderResponse);
        folderResponse.setTypeCN(folderTypeProperties.getTypeNameById(folderResponse.getType()));
        return ResultModelUtil.getSuccessInstance(folderResponse);
    }

    @Override
    public ResultModel<FolderResponse> update(FolderRequest folderRequest) {
        /**
         * 按id查询文件夹
         */
        Optional<FolderInfo> folderInfoOptional = folderRepository.findById(Long.parseLong(folderRequest.getId()));
        if (!folderInfoOptional.isPresent()) {
            return ResultModelUtil.getFailInstance(CodeEnum.NO_DATA, "未获取到文件夹信息");
        }
        FolderInfo folderInfo = folderInfoOptional.get();
        /**
         * 比对是否修改文件夹名称
         */
        if (!StringUtils.isBlank(folderRequest.getName()) && !CommonUtil.compare(folderRequest.getName(), folderInfo.getName())) {
            folderRepository.updateName(Long.parseLong(folderRequest.getId()), folderRequest.getName());
        }
        /**
         * 比对是否修改文件夹类型
         */
        if (folderRequest.getType() != null && !CommonUtil.compare(folderRequest.getType(), folderInfo.getType())) {
            folderRepository.updateType(Long.parseLong(folderRequest.getId()), folderRequest.getType());
        }
        /**
         * 比对是否修改备注
         */
        if (!StringUtils.isBlank(folderRequest.getRemark()) && !CommonUtil.compare(folderRequest.getRemark(), folderInfo.getRemark())) {
            folderRepository.updateRemark(Long.parseLong(folderRequest.getId()), folderRequest.getRemark());
        }
        FolderResponse folderResponse = new FolderResponse();
        ModelCover.folderPOToResponse(folderInfo, folderResponse);
        folderResponse.setTypeCN(folderTypeProperties.getTypeNameById(folderResponse.getType()));
        return ResultModelUtil.getSuccessInstance(folderResponse);
    }

    @Override
    public ResultModel updatePid(FolderRequest folderRequest) {
        FolderInfo folderInfo = new FolderInfo();
        ModelCover.folderRequestToPO(folderRequest, folderInfo);
        /**
         * 查询父文件夹
         */
        FolderInfo parentFolder = findParentFolder(folderInfo.getPid());
        if (-1 != folderInfo.getPid() && null == parentFolder) {
            return ResultModelUtil.getFailInstance(CodeEnum.PARAM_ERROR, "父文件夹id无效");
        }
        int count = folderRepository.updatePid(folderInfo.getId(), folderInfo.getPid());
        if (0 == count) {
            return ResultModelUtil.getFailInstance(CodeEnum.ERROR, "修改父文件夹id失败");
        }
        /**
         * 全部层级文件夹同步父文件夹共享状态与只读状态
         */
        folderInfo.setBeShare(parentFolder.getBeShare());
        folderInfo.setBeReadOnly(parentFolder.getBeReadOnly());
        updateAllChildFolderState(folderInfo);
        FolderResponse folderResponse = new FolderResponse();
        ModelCover.folderPOToResponse(folderInfo, folderResponse);
        folderResponse.setTypeCN(folderTypeProperties.getTypeNameById(folderResponse.getType()));
        return ResultModelUtil.getSuccessInstance(folderResponse);
    }

    @Override
    public ResultModel updateShare(FolderRequest folderRequest) {
        /**
         * 修改是否共享
         * 开启共享时，若其上全部层级文件夹中有共享文件夹，则将两文件夹之前全部层级文件夹设为共享
         * 开始共享时，将其下全部层级文件夹设为共享
         * 关闭共享时，将其下全部层级文件夹设为非共享
         */
        /**
         * 查询文件夹及其下全部层级文件夹
         */
        List<FolderInfo> folderInfos = findAllFolder(Long.parseLong(folderRequest.getId()), null);
        if (CollectionUtils.isEmpty(folderInfos)) {
            return ResultModelUtil.getFailInstance(CodeEnum.NO_DATA, "未获取到文件夹");
        }
        /**
         * 设置全部层级文件夹共享状态
         */
        folderInfos.stream().forEach(folderInfo -> folderInfo.setBeShare(folderRequest.getBeShare()));
        folderRepository.saveAll(folderInfos).size();
        if (0 == folderRequest.getBeShare()) {
            return ResultModelUtil.getSuccessInstance("修改成功");
        }
        /**
         * 修改其上全部层级非共享文件夹共享状态
         */
        FolderInfo folderInfo = folderInfos.get(0);
        folderInfos = new ArrayList<>(1);
        updateParentShare(folderInfo.getPid(), folderInfos);
        if (CollectionUtils.isEmpty(folderInfos)) {
            return ResultModelUtil.getSuccessInstance("修改成功");
        }
        folderRepository.saveAll(folderInfos).size();
        return ResultModelUtil.getSuccessInstance("修改成功");
    }

    @Override
    public ResultModel updateReadOnly(FolderRequest folderRequest) {
        /**
         * 修改是否只读
         * 关闭只读时，若其父文件夹为只读文件夹，则禁止关闭
         * 关闭只读时，将其下全部层级文件夹设为非只读
         * 开启只读时，将其下全部层级文件夹设为只读
         */
        if (0 == folderRequest.getBeReadOnly()) {
            Optional<FolderInfo> folderInfoOptional = folderRepository.findById(Long.parseLong(folderRequest.getId()));
            if (!folderInfoOptional.isPresent()) {
                return ResultModelUtil.getFailInstance(CodeEnum.PARAM_ERROR, "文件夹不存在");
            }
            boolean flag = findParentReadOnly(folderInfoOptional.get().getPid());
            if (!flag) {
                return ResultModelUtil.getFailInstance(CodeEnum.ERROR, "上级文件夹为只读状态");
            }
        }
        int count = updateAllFolderReadOnly(Long.parseLong(folderRequest.getId()), folderRequest.getBeReadOnly());
        if (0 == count) {
            return ResultModelUtil.getFailInstance(CodeEnum.ERROR, "修改文件夹只读状态失败");
        }
        return ResultModelUtil.getSuccessInstance("修改成功");
    }

    @Override
    public ResultModel deleteFake(JSONArray ids) {
        List<FolderInfo> folderInfos = new ArrayList<>(1);
        /**
         * 查询文件夹及其下全部层级子文件夹
         */
        List<FolderInfo> folders;
        for (Object id : ids) {
            folders = findAllFolder(Long.parseLong(id.toString()), 0);
            if (CollectionUtils.isEmpty(folders)) {
                return ResultModelUtil.getFailInstance(CodeEnum.PARAM_ERROR, "文件夹id无效");
            }
            folderInfos.addAll(folders);
        }
        folderInfos.stream().forEach(folderInfo -> folderInfo.setDel(1));
        folderRepository.saveAll(folderInfos);
        return ResultModelUtil.getSuccessInstance("删除成功");
    }

    @Override
    public ResultModel deleteReal(JSONArray ids) {
        List<FolderInfo> folderInfos = new ArrayList<>(1);
        /**
         * 查询文件夹及其下全部层级子文件夹
         */
        List<FolderInfo> folders;
        for (Object id : ids) {
            folders = findAllFolder(Long.parseLong(id.toString()), null);
            if (CollectionUtils.isEmpty(folders)) {
                return ResultModelUtil.getFailInstance(CodeEnum.PARAM_ERROR, "文件夹id无效");
            }
            folderInfos.addAll(folders);
        }
        folderRepository.deleteAll(folderInfos);
        return ResultModelUtil.getSuccessInstance("删除成功");
    }

    @Override
    public ResultModel findFolder(String appKey, String ownId, String pid, Integer isFindAll, Integer isBuildTree) {
        List<FolderResponse> folderResponses = new ArrayList<>(1);
        FolderInfo folderInfo = new FolderInfo();
        folderInfo.setAppKey(appKey);
        folderInfo.setOwnId(ownId);
        folderInfo.setPid(Long.parseLong(pid));
        folderInfo.setDel(0);
        List<FolderInfo> folderInfos = folderRepository.findByAppKeyAndOwnIdAndPidAndDel(
                folderInfo.getAppKey(), folderInfo.getOwnId(), folderInfo.getPid(), folderInfo.getDel());
        if (CollectionUtils.isEmpty(folderInfos)) {
            return ResultModelUtil.getFailInstance(CodeEnum.NO_DATA, "未查询到文件夹");
        }
        ModelCover.folderPOsToResponses(folderInfos, folderResponses);
        /**
         * 若isFindAll为0，则直接返回当前查询文件夹
         * 若isFindAll为1，则继续查询其下全部层级文件夹
         */
        if (0 == isFindAll) {
            folderResponses.stream().forEach(folderResponse -> folderResponse.setTypeCN(folderTypeProperties.getTypeNameById(folderResponse.getType())));
            return ResultModelUtil.getSuccessInstance(folderResponses);
        }
        /**
         * 若isBuildTree为0，则不组装树结构，直接返回集合
         * 若isBuildTree为1，则组装为树结构返回
         */
        if (0 == isBuildTree) {
            List<FolderResponse> folderResponseList = new ArrayList<>(1);
            folderResponseList.addAll(folderResponses);
            folderResponses.stream().forEach(folderResponse -> findAllChildNotBuild(folderResponseList, folderResponse, folderInfo));
            return ResultModelUtil.getSuccessInstance(folderResponseList);
        }
        folderResponses.stream().forEach(folderResponse -> findAllChildAndBuild(folderResponse, folderInfo));
        return ResultModelUtil.getSuccessInstance(folderResponses);
    }

    @Override
    public ResultModel findShareFolder(String appKey, String pid, Integer isFindAll, Integer isBuildTree) {
        List<FolderResponse> folderResponses = new ArrayList<>(1);
        FolderInfo folderInfo = new FolderInfo();
        folderInfo.setAppKey(appKey);
        if (!StringUtils.isBlank(pid)) {
            folderInfo.setPid(Long.parseLong(pid));
        }
        folderInfo.setBeShare(1);
        folderInfo.setDel(0);
        /**
         * 查询共享文件夹
         */
        List<FolderInfo> folderInfos = findShareFolder(folderInfo);
        if (CollectionUtils.isEmpty(folderInfos)) {
            return ResultModelUtil.getFailInstance(CodeEnum.NO_DATA, "未查询到共享文件夹");
        }
        ModelCover.folderPOsToResponses(folderInfos, folderResponses);
        /**
         * 若isFindAll为0，则直接返回当前查询文件夹
         * 若isFindAll为1，则继续查询其下全部层级文件夹
         */
        if (0 == isFindAll) {
            folderResponses.stream().forEach(folderResponse -> folderResponse.setTypeCN(folderTypeProperties.getTypeNameById(folderResponse.getType())));
            return ResultModelUtil.getSuccessInstance(folderResponses);
        }
        /**
         * 若isBuildTree为0，则不组装树结构，直接返回集合
         * 若isBuildTree为1，则组装为树结构返回
         */
        if (0 == isBuildTree) {
            List<FolderResponse> folderResponseList = new ArrayList<>(1);
            folderResponseList.addAll(folderResponses);
            folderResponses.stream().forEach(folderResponse -> findAllShareChildNotBuild(folderResponseList, folderResponse, folderInfo));
            return ResultModelUtil.getSuccessInstance(folderResponseList);
        }
        folderResponses.stream().forEach(folderResponse -> findAllShareChildAndBuild(folderResponse, folderInfo));
        return ResultModelUtil.getSuccessInstance(folderResponses);
    }

    /**
     * 查询父文件夹
     *
     * @param id
     */
    private FolderInfo findParentFolder(Long id) {
        Optional<FolderInfo> parentFolder = folderRepository.findById(id);
        if (parentFolder.isPresent()) {
            return parentFolder.get();
        }
        return null;
    }

    /**
     * 修改全部层级子文件夹状态
     *
     * @param folderInfo
     * @return
     */
    private void updateAllChildFolderState(FolderInfo folderInfo) {
        /**
         * 查询全部层级文件夹
         */
        List<FolderInfo> folderInfos = findAllFolder(folderInfo.getId(), null);
        if (CollectionUtils.isEmpty(folderInfos)) {
            return;
        }
        /**
         * 同步全部层级文件夹共享状态与只读状态
         */
        folderInfos.stream().forEach(f -> {
            f.setBeShare(folderInfo.getBeShare());
            f.setBeReadOnly(folderInfo.getBeReadOnly());
        });
        folderRepository.saveAll(folderInfos);
        return;
    }

    /**
     * 查询全部层级文件夹
     *
     * @param id
     * @param del
     * @return
     */
    private List<FolderInfo> findAllFolder(Long id, Integer del) {
        List<FolderInfo> folderInfos = new ArrayList<>(1);
        /**
         * 按id查询文件夹
         */
        Optional<FolderInfo> folderInfoOptional = folderRepository.findById(id);
        if (!folderInfoOptional.isPresent()) {
            return folderInfos;
        }
        folderInfos.add(folderInfoOptional.get());
        /**
         * 递归查询全部层级子文件夹
         */
        findAllChildFolder(id, del, folderInfos);
        return folderInfos;
    }

    /**
     * 递归查询全部层级子文件夹
     *
     * @param pid
     * @param del
     * @param folderInfos
     */
    private void findAllChildFolder(Long pid, Integer del, List<FolderInfo> folderInfos) {
        List<FolderInfo> childFolders;
        if (null == del) {
            childFolders = folderRepository.findByPid(pid);
        } else {
            childFolders = folderRepository.findByPidAndDel(pid, del);
        }
        if (CollectionUtils.isEmpty(childFolders)) {
            return;
        }
        folderInfos.addAll(childFolders);
        childFolders.stream().forEach(childFolder -> findAllChildFolder(childFolder.getId(), del, folderInfos));
    }

    /**
     * 修改父文件夹共享状态
     *
     * @param id
     * @param folderInfos
     */
    private void updateParentShare(Long id, List<FolderInfo> folderInfos) {
        Optional<FolderInfo> folderInfoOptional = folderRepository.findById(id);
        if (!folderInfoOptional.isPresent()) {
            return;
        }
        FolderInfo folderInfo = folderInfoOptional.get();
        if (1 == folderInfo.getBeShare()) {
            return;
        }
        folderInfo.setBeShare(1);
        folderInfos.add(folderInfo);
        updateParentShare(folderInfo.getPid(), folderInfos);
    }

    /**
     * 查询父文件夹只读状态
     *
     * @param id
     * @return
     */
    private boolean findParentReadOnly(long id) {
        Optional<FolderInfo> folderInfoOptional = folderRepository.findById(id);
        if (!folderInfoOptional.isPresent()) {
            return true;
        }
        FolderInfo folderInfo = folderInfoOptional.get();
        if (1 == folderInfo.getBeReadOnly()) {
            return false;
        }
        return true;
    }

    /**
     * 修改文件夹及其下全部层级子文件夹只读状态
     *
     * @param id
     * @param beReadOnly
     * @return
     */
    private int updateAllFolderReadOnly(Long id, Integer beReadOnly) {
        /**
         * 查询文件夹及其下全部层级子文件夹
         */
        List<FolderInfo> folderInfos = findAllFolder(id, null);
        if (CollectionUtils.isEmpty(folderInfos)) {
            return 0;
        }
        folderInfos.stream().forEach(folderInfo -> folderInfo.setBeReadOnly(beReadOnly));
        return folderRepository.saveAll(folderInfos).size();
    }

    /**
     * 查询全部层级子文件夹（不组装为树结构）
     *
     * @param folderResponses
     * @param folderResponse
     * @param folderInfo
     */
    private void findAllChildNotBuild(List<FolderResponse> folderResponses
            , FolderResponse folderResponse
            , FolderInfo folderInfo) {
        folderResponse.setTypeCN(folderTypeProperties.getTypeNameById(folderResponse.getType()));
        /**
         * 查询子文件夹
         */
        List<FolderInfo> childFolderInfos = folderRepository.findByAppKeyAndOwnIdAndPidAndDel(folderInfo.getAppKey()
                , folderInfo.getOwnId(), Long.parseLong(folderResponse.getId()), folderInfo.getDel());
        if (CollectionUtils.isEmpty(childFolderInfos)) {
            return;
        }
        List<FolderResponse> childFolderResponses = new ArrayList<>(1);
        ModelCover.folderPOsToResponses(childFolderInfos, childFolderResponses);
        /**
         * 将子文件夹集合直接追加进返回结果集合
         */
        folderResponses.addAll(childFolderResponses);
        /**
         * 递归查询
         */
        childFolderResponses.stream().forEach(fr -> findAllChildNotBuild(folderResponses, fr, folderInfo));
    }

    /**
     * 查询全部层级子文件夹（组装为树结构）
     *
     * @param folderResponse
     * @param folderInfo
     */
    private void findAllChildAndBuild(FolderResponse folderResponse, FolderInfo folderInfo) {
        folderResponse.setTypeCN(folderTypeProperties.getTypeNameById(folderResponse.getType()));
        List<FolderInfo> childFolderInfos = folderRepository.findByAppKeyAndOwnIdAndPidAndDel(folderInfo.getAppKey()
                , folderInfo.getOwnId(), Long.parseLong(folderResponse.getId()), folderInfo.getDel());
        if (CollectionUtils.isEmpty(childFolderInfos)) {
            return;
        }
        List<FolderResponse> childFolderResponses = new ArrayList<>(1);
        ModelCover.folderPOsToResponses(childFolderInfos, childFolderResponses);
        folderResponse.getChildFolders().addAll(childFolderResponses);
        childFolderResponses.stream().forEach(fr -> findAllChildAndBuild(fr, folderInfo));
    }

    /**
     * 查询共享文件夹
     *
     * @return
     */
    private List<FolderInfo> findShareFolder(FolderInfo folderInfo) {
        List<FolderInfo> shareFolderInfos;
        /**
         * 若pid为空，则查询全部共享文件夹并筛选出第一层共享文件夹
         * 若pid不为空，则按pid查询子共享文件夹
         */
        if (null == folderInfo.getPid()) {
            shareFolderInfos = folderRepository.findByAppKeyAndBeShareAndDel(folderInfo.getAppKey()
                    , folderInfo.getBeShare()
                    , folderInfo.getDel());
            shareFolderInfos = getRootShareFolder(shareFolderInfos);
            return shareFolderInfos;
        }
        shareFolderInfos = folderRepository.findByPidAndBeShareAndDel(folderInfo.getPid()
                , folderInfo.getBeShare()
                , folderInfo.getDel());
        return shareFolderInfos;
    }

    /**
     * 获取第一层共享文件夹
     *
     * @param folderInfos
     * @return
     */
    private List<FolderInfo> getRootShareFolder(List<FolderInfo> folderInfos) {
        List<FolderInfo> rootFolder = new ArrayList<>(1);
        if (CollectionUtils.isEmpty(folderInfos)) {
            return rootFolder;
        }
        /**
         * 获取pid不在文件夹id集合之内的第一层共享文件夹
         */
        Set<Long> folderIds = folderInfos.stream().map(FolderInfo::getId).collect(Collectors.toSet());
        for (FolderInfo folderInfo : folderInfos) {
            if (folderIds.contains(folderInfo.getPid())) {
                continue;
            }
            rootFolder.add(folderInfo);
        }
        return rootFolder;
    }

    /**
     * 查询全部层级共享子文件夹（不组装为树结构）
     *
     * @param folderResponses
     * @param folderResponse
     * @param folderInfo
     */
    private void findAllShareChildNotBuild(List<FolderResponse> folderResponses
            , FolderResponse folderResponse
            , FolderInfo folderInfo) {
        folderResponse.setTypeCN(folderTypeProperties.getTypeNameById(folderResponse.getType()));
        /**
         * 查询子文件夹
         */
        List<FolderInfo> childFolderInfos = folderRepository.findByPidAndBeShareAndDel(
                Long.parseLong(folderResponse.getId()), folderInfo.getBeShare(), folderInfo.getDel());
        if (CollectionUtils.isEmpty(childFolderInfos)) {
            return;
        }
        List<FolderResponse> childFolderResponses = new ArrayList<>(1);
        ModelCover.folderPOsToResponses(childFolderInfos, childFolderResponses);
        /**
         * 将子文件夹集合直接追加进返回结果集合
         */
        folderResponses.addAll(childFolderResponses);
        /**
         * 递归查询
         */
        childFolderResponses.stream().forEach(fr -> findAllShareChildNotBuild(folderResponses, fr, folderInfo));
    }

    /**
     * 查询全部层级共享子文件夹（组装为树结构）
     *
     * @param folderResponse
     * @param folderInfo
     */
    private void findAllShareChildAndBuild(FolderResponse folderResponse, FolderInfo folderInfo) {
        folderResponse.setTypeCN(folderTypeProperties.getTypeNameById(folderResponse.getType()));
        List<FolderInfo> childFolderInfos = folderRepository.findByPidAndBeShareAndDel(
                Long.parseLong(folderResponse.getId()), folderInfo.getBeShare(), folderInfo.getDel());
        if (CollectionUtils.isEmpty(childFolderInfos)) {
            return;
        }
        List<FolderResponse> childFolderResponses = new ArrayList<>(1);
        ModelCover.folderPOsToResponses(childFolderInfos, childFolderResponses);
        folderResponse.getChildFolders().addAll(childFolderResponses);
        childFolderResponses.stream().forEach(fr -> findAllShareChildAndBuild(fr, folderInfo));
    }
}