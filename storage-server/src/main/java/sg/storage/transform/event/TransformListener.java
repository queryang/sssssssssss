package sg.storage.transform.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import sg.storage.model.vo.file.TransformRequest;

import java.util.List;

/**
 * 转化监听
 */
@Slf4j
@Component
public class TransformListener implements ApplicationListener<TransformEvent> {

    private TransformQueue transformQueue;

    @Override
    public void onApplicationEvent(TransformEvent transformEvent) {
        List<TransformRequest> transformRequests = transformEvent.getTransformRequests();
        transformQueue = TransformQueue.getInstance();
        for (TransformRequest transformRequest : transformRequests) {
            if (null == transformRequest) {
                continue;
            }
            transformQueue.addQueue(transformRequest);
            log.info("追加文件转化请求: 【源文件id】{}-【源文件类型】{}-【转化文件后缀】{}-【转化文件尺寸类型】{}"
                    , transformRequest.getOriginFileId()
                    , transformRequest.getOriginFileType()
                    , transformRequest.getTransformFileSuffix()
                    , transformRequest.getTransformFileSizeType());
        }
        log.info("开始文件转化");
        transformQueue.transformQueue();
        log.info("文件转化结束");
    }
}