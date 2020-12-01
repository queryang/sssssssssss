package sg.storage.transform.creator.impl;

import org.springframework.stereotype.Component;
import sg.storage.common.properties.FileConstant;
import sg.storage.transform.creator.TransformRequestUtil;
import sg.storage.model.vo.file.TransformRequest;
import sg.storage.model.po.FileInfo;
import sg.storage.transform.creator.TransformRequestCreator;

import java.util.ArrayList;
import java.util.List;

/**
 * 图片转化图片
 */
@Component
public class TransformImageToImage implements TransformRequestCreator {

    @Override
    public List<TransformRequest> getTransformRequests(FileInfo fileInfo
            , String basePath, String transformSuffix, String transformClass) {
        List<TransformRequest> transformRequests = new ArrayList<>(1);
        /**
         * 若当前文件后缀与转化文件后缀相同则略过o原尺寸
         */
        int i = 0;
        if (fileInfo.getFileSuffix().equalsIgnoreCase(transformSuffix)) {
            i = 1;
        }
        TransformRequest transformRequest;
        for (int size = FileConstant.IMAGE_SIZES.size(); i < size; i++) {
            transformRequest = TransformRequestUtil.createTransformRequest(fileInfo
                    , basePath, transformSuffix, FileConstant.IMAGE_SIZES.get(i), transformClass);
            transformRequests.add(transformRequest);
        }
        return transformRequests;
    }
}