package sg.storage.transform.service.impl.openOffice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sg.storage.common.properties.OpenOfficeProperties;
import sg.storage.transform.service.ITransformService;
import sg.storage.model.vo.file.TransformRequest;

/**
 * OpenOffice转化
 */
@Slf4j
@Component
public class OpenOfficeTransform implements ITransformService {

    private static OpenOfficeProperties openOfficeProperties;

    public OpenOfficeTransform() {
    }

    @Autowired
    public OpenOfficeTransform(OpenOfficeProperties openOfficeConfiguration) {
        OpenOfficeTransform.openOfficeProperties = openOfficeConfiguration;
    }

    @Override
    public void transform(TransformRequest transformRequest) throws Exception {
        OpenOfficeUtil.convertFile(openOfficeProperties.getIp()
                , openOfficeProperties.getPort()
                , transformRequest.getOriginFilePath()
                , transformRequest.getTransformFilePath());
    }
}