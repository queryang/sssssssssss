package sg.storage.transform.event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;
import sg.storage.model.vo.file.TransformRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 转化事件
 */
@Getter
@Setter
public class TransformEvent extends ApplicationEvent {

    private static final long serialVersionUID = 1L;

    private List<TransformRequest> transformRequests = new ArrayList<>(1);

    public TransformEvent(Object source, TransformRequest... transformRequest) {
        super(source);
        this.transformRequests.addAll(Arrays.asList(transformRequest));
    }

    public TransformEvent(Object source, List<TransformRequest> transformRequests) {
        super(source);
        this.transformRequests.addAll(transformRequests);
    }
}