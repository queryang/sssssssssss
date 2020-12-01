package sg.storage.web.kafka;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import sg.storage.common.properties.KafkaConstant;
import sg.storage.transform.event.TransformPublisher;
import sg.storage.model.vo.file.TransformRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class KafkaReceiver {

    @Autowired
    private TransformPublisher transformPublisher;

    @KafkaListener(topics = {KafkaConstant.TRANSFORM_TOPIC})
    public void listen(ConsumerRecord<?, ?> record) {
        Optional<?> kafkaMessage = Optional.ofNullable(record.value());
        if (kafkaMessage.isPresent()) {
            Object message = kafkaMessage.get();
            List<TransformRequest> transformRequests = new ArrayList<>(1);
            JSONArray jsonArray = JSON.parseArray(message.toString());
            jsonArray.stream().forEach(json -> {
                transformRequests.add(JSON.toJavaObject((JSON) json, TransformRequest.class));
            });
            transformPublisher.publish(transformRequests);
        }
    }
}