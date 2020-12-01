package sg.storage.transform.creator;

import sg.storage.model.vo.file.TransformRequest;
import sg.storage.model.po.FileInfo;

import java.util.List;

/**
 * 转化请求创建
 */
public interface TransformRequestCreator {

    /**
     * 获取转化请求
     *
     * @param fileInfo        文件信息
     * @param basePath        基础存储路径
     * @param transformSuffix 转化文件后缀
     * @param transformClass  转化实现类
     * @return
     */
    List<TransformRequest> getTransformRequests(FileInfo fileInfo
            , String basePath, String transformSuffix, String transformClass);
}