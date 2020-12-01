package sg.storage.transform.service.impl;

import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.EncodingAttributes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import sg.storage.transform.service.ITransformService;
import sg.storage.model.vo.file.TransformRequest;

import java.io.File;

/**
 * 音频视频转码
 */
@Slf4j
@Component
public abstract class Transcode implements ITransformService {

    private Encoder encoder = new Encoder();

    @Override
    public void transform(TransformRequest transformRequest) throws Exception {
        EncodingAttributes encodingAttributes = getEncodeAttr(transformRequest);
        encoder.encode(new File(transformRequest.getOriginFilePath())
                , new File(transformRequest.getTransformFilePath())
                , encodingAttributes);
    }

    public abstract EncodingAttributes getEncodeAttr(TransformRequest transformRequest);
}