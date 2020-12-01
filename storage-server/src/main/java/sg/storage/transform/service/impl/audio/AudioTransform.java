package sg.storage.transform.service.impl.audio;

import it.sauronsoftware.jave.AudioAttributes;
import it.sauronsoftware.jave.EncodingAttributes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import sg.storage.model.vo.file.TransformRequest;
import sg.storage.transform.service.impl.Transcode;

/**
 * 音频转化
 */
@Slf4j
@Component
public class AudioTransform extends Transcode {

    @Override
    public EncodingAttributes getEncodeAttr(TransformRequest transformRequest) {
        AudioAttributes audio = new AudioAttributes();
        audio.setCodec("libmp3lame");
        EncodingAttributes attrs = new EncodingAttributes();
        attrs.setFormat(null);
        attrs.setAudioAttributes(audio);
        return attrs;
    }
}