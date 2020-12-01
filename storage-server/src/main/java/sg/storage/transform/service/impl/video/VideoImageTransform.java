package sg.storage.transform.service.impl.video;

import it.sauronsoftware.jave.EncodingAttributes;
import it.sauronsoftware.jave.VideoAttributes;
import it.sauronsoftware.jave.VideoSize;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import sg.storage.common.properties.TransformFileSizeProperties;
import sg.storage.model.vo.file.TransformRequest;
import sg.storage.transform.service.impl.Transcode;

/**
 * 视频截图转化
 */
@Slf4j
@Component
public class VideoImageTransform extends Transcode {

    @Override
    public EncodingAttributes getEncodeAttr(TransformRequest transformRequest) {
        VideoAttributes video = new VideoAttributes();
        video.setSize(getVideoImageSize(transformRequest));
        EncodingAttributes attrs = new EncodingAttributes();
        attrs.setDuration(0.01f);
        attrs.setFormat("image2");
        attrs.setVideoAttributes(video);
        return attrs;
    }

    /**
     * 获取视频截图转化尺寸
     *
     * @param transformRequest 转化请求
     * @return
     */
    private VideoSize getVideoImageSize(TransformRequest transformRequest) {
        TransformFileSizeProperties.Size size = TransformFileSizeProperties.getVoideSize(transformRequest.getTransformFileSizeType());
        return new VideoSize(size.getWidth().intValue(), size.getHeight().intValue());
    }
}