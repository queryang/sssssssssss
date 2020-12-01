package sg.storage.transform.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import sg.storage.model.vo.file.TransformRequest;

import java.util.List;

/**
 * 转化发布
 */
@Component
public class TransformPublisher {

    @Autowired
    private ApplicationContext applicationContext;

    public void publish(TransformRequest... transformRequest) {
        TransformEvent transformEvent = new TransformEvent(this, transformRequest);
        applicationContext.publishEvent(transformEvent);
    }

    public void publish(List<TransformRequest> transformRequests) {
        TransformEvent transformEvent = new TransformEvent(this, transformRequests);
        applicationContext.publishEvent(transformEvent);
    }
}