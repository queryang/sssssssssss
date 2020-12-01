package sg.storage.service;

import com.alibaba.fastjson.JSONArray;
import sg.storage.common.entity.ResultModel;
import sg.storage.model.po.FileInfo;

/**
 * 文件管理
 */
public interface IFileService {

    ResultModel insert(FileInfo fileInfo);

    /**
     * 删除文件
     *
     * @param ids
     * @return
     */
    ResultModel deleteReal(JSONArray ids);

    /**
     * 删除文件（数据标记）
     *
     * @param ids
     * @return
     */
    ResultModel deleteLogic(JSONArray ids);

    /**
     * 复制文件
     *
     * @param id
     * @param folderId
     * @return
     */
    ResultModel copyFile(String id, String folderId);

    /**
     * 修改文件所属文件夹
     *
     * @param id
     * @param folderId
     * @return
     */
    ResultModel updateFileFolder(String id, String folderId);

    /**
     * 按文件id查询文件
     *
     * @param id
     * @return
     */
    FileInfo findById(String id);

    /**
     * 按id查询转化文件
     *
     * @param id
     * @return
     */
    ResultModel findTrans(String id);

    /**
     * 按文件夹id查询文件
     *
     * @param folderId
     * @return
     */
    ResultModel findByFolderId(String folderId);

    /**
     * 按文件夹id分页查询文件
     *
     * @param folderId
     * @param pageNum
     * @param pageSize
     * @return
     */
    ResultModel findPageByFolderId(String folderId, Integer pageNum, Integer pageSize);

    /**
     * 删除文件夹下文件
     *
     * @param folderId
     * @return
     */
    ResultModel deleteFolderFile(String folderId);
}