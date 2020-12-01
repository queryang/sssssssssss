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
 * 视频转化图片
 */
@Component
public class TransformVideoToImage implements TransformRequestCreator {

    @Override
    public List<TransformRequest> getTransformRequests(FileInfo fileInfo
            , String basePath, String transformSuffix, String transformClass) {
        List<TransformRequest> transformRequests = new ArrayList<>(1);
        TransformRequest transformRequest;
        for (int i = 0, size = FileConstant.VIDEO_SIZES.size(); i < size; i++) {
            transformRequest = TransformRequestUtil.createTransformRequest(fileInfo
                    , basePath, transformSuffix, FileConstant.VIDEO_SIZES.get(i), transformClass);
            transformRequests.add(transformRequest);
        }
        return transformRequests;
    }
}