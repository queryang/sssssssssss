package sg.storage.transform.creator.impl;

import org.springframework.stereotype.Component;
import sg.storage.model.vo.file.TransformRequest;
import sg.storage.model.po.FileInfo;
import sg.storage.transform.creator.TransformRequestCreator;
import sg.storage.transform.creator.TransformRequestUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 默认转化
 */
@Component
public class DefaultTransform implements TransformRequestCreator {

    @Override
    public List<TransformRequest> getTransformRequests(FileInfo fileInfo
            , String basePath, String transformSuffix, String transformClass) {
        List<TransformRequest> transformRequests = new ArrayList<>(1);
        /**
         * 若当前文件后缀与转化文件后缀相同则不需进行转化
         */
        if (fileInfo.getFileSuffix().equalsIgnoreCase(transformSuffix)) {
            return transformRequests;
        }
        transformRequests.add(TransformRequestUtil.createTransformRequest(fileInfo
                , basePath, transformSuffix, transformClass));
        return transformRequests;
    }
}