package sg.storage.service;

import com.alibaba.fastjson.JSONArray;
import sg.storage.common.entity.ResultModel;
import sg.storage.model.vo.folder.FolderRequest;
import sg.storage.model.vo.folder.FolderResponse;

import java.util.List;

public interface IFolderService {

    /**
     * 新增文件夹
     *
     * @param folderRequest
     * @return
     */
    ResultModel<FolderResponse> insert(FolderRequest folderRequest);

    /**
     * 修改文件夹
     *
     * @param folderRequest
     * @return
     */
    ResultModel<FolderResponse> update(FolderRequest folderRequest);

    /**
     * 修改父文件夹id
     *
     * @param folderRequest
     * @return
     */
    ResultModel<FolderResponse> updatePid(FolderRequest folderRequest);

    /**
     * 修改文件夹共享状态
     *
     * @param folderRequest
     * @return
     */
    ResultModel<FolderResponse> updateShare(FolderRequest folderRequest);

    /**
     * 修改文件夹只读状态
     *
     * @param folderRequest
     * @return
     */
    ResultModel<FolderResponse> updateReadOnly(FolderRequest folderRequest);

    /**
     * 删除文件夹（逻辑删除）
     *
     * @param ids
     * @return
     */
    ResultModel<FolderResponse> deleteFake(JSONArray ids);

    /**
     * 删除文件夹（物理删除）
     *
     * @param ids
     * @return
     */
    ResultModel<FolderResponse> deleteReal(JSONArray ids);

    /**
     * 查询文件夹
     *
     * @param appKey
     * @param ownId
     * @param pid
     * @param isFindAll
     * @param isBuildTree
     * @return
     */
    ResultModel<List<FolderResponse>> findFolder(String appKey, String ownId, String pid, Integer isFindAll, Integer isBuildTree);

    /**
     * 查询共享文件夹
     *
     * @param appKey
     * @param pid
     * @param isFindAll
     * @param isBuildTree
     * @return
     */
    ResultModel<List<FolderResponse>> findShareFolder(String appKey, String pid, Integer isFindAll, Integer isBuildTree);
}