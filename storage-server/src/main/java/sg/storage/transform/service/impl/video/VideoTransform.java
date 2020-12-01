package sg.storage.transform.service.impl.video;

import it.sauronsoftware.jave.AudioAttributes;
import it.sauronsoftware.jave.EncodingAttributes;
import it.sauronsoftware.jave.VideoAttributes;
import it.sauronsoftware.jave.VideoSize;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import sg.storage.common.properties.TransformFileSizeProperties;
import sg.storage.model.vo.file.TransformRequest;
import sg.storage.transform.service.impl.Transcode;

/**
 * 视频转化
 */
@Slf4j
@Component
public class VideoTransform extends Transcode {

    @Override
    public EncodingAttributes getEncodeAttr(TransformRequest transformRequest) {
        AudioAttributes audio = new AudioAttributes();
        VideoAttributes video = new VideoAttributes();
        video.setCodec("mpeg4");
        video.setSize(getVideoSize(transformRequest));
        EncodingAttributes attrs = new EncodingAttributes();
        attrs.setFormat("mp4");
        attrs.setAudioAttributes(audio);
        attrs.setVideoAttributes(video);
        return attrs;
    }

    /**
     * 获取视频转化尺寸
     *
     * @param transformRequest
     * @return
     */
    private VideoSize getVideoSize(TransformRequest transformRequest) {
        TransformFileSizeProperties.Size size = TransformFileSizeProperties.getVoideSize(transformRequest.getTransformFileSizeType());
        return new VideoSize(size.getWidth().intValue(), size.getHeight().intValue());
    }
}