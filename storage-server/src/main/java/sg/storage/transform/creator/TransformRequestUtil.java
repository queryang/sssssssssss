package sg.storage.transform.creator;

import sg.storage.common.properties.FileConstant;
import sg.storage.common.creator.FilePathCreator;
import sg.storage.model.vo.file.TransformRequest;
import sg.storage.model.po.FileInfo;

public class TransformRequestUtil {

    /**
     * 创建转化请求
     *
     * @param fileInfo        文件信息
     * @param basePath        基础存储路径
     * @param transformSuffix 转化文件后缀
     * @param transformClass  转化实现类
     * @return
     */
    public static TransformRequest createTransformRequest(FileInfo fileInfo
            , String basePath, String transformSuffix, String transformClass) {
        return createTransformRequest(fileInfo, basePath, transformSuffix, FileConstant.ORIGINAL, transformClass);
    }

    /**
     * 创建转化请求
     *
     * @param fileInfo          文件信息
     * @param basePath          基础存储路径
     * @param transformSuffix   转化文件后缀
     * @param transformSizeType 转化文件尺寸
     * @param transformClass    转化实现类
     * @return
     */
    public static TransformRequest createTransformRequest(FileInfo fileInfo
            , String basePath, String transformSuffix, String transformSizeType, String transformClass) {
        TransformRequest transformRequest = new TransformRequest();
        transformRequest.setOriginFileId(String.valueOf(fileInfo.getId()));
        transformRequest.setOriginFileType(fileInfo.getFileType());
        transformRequest.setOriginFilePath(fileInfo.getFilePath());
        transformRequest.setTransformFileSuffix(transformSuffix);
        transformRequest.setTransformFileSizeType(transformSizeType);
        transformRequest.setTransformClass(transformClass);
        transformRequest.setTransformFilePath(FilePathCreator.getFilePath(fileInfo, basePath, transformSuffix, transformSizeType));
        return transformRequest;
    }
}