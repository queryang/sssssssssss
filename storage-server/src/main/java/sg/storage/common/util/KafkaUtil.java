package sg.storage.common.util;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Kafka
 */
@Slf4j
@Component
public class KafkaUtil {

    private static KafkaTemplate template;

    @Autowired
    private KafkaUtil(KafkaTemplate template) {
        KafkaUtil.template = template;
    }

    /**
     * @Author: zmh
     * @Description: 异步发送kafka消息
     * @Date: 2020/5/9 14:47
     * @Version:1.0
     */
    public static void sendMessage(String topic, String msg) {
        template.send(topic, msg);
    }

    /**
     * @Author: zmh
     * @Description: 发送消息  可以参考 方法：sendSync
     * @Date: 2020/5/9 17:28
     * @Version:1.0
     */
    public static ListenableFuture<SendResult<String, String>> send(String topic, String msg) {
        return template.send(topic, msg);
    }

    /**
     * @Author: zmh
     * @Description: 同步发送消息
     * @Date: 2020/5/9 17:21
     * @Version:1.0
     */
    public static boolean sendSyncEasy(String topic, String msg) {
        try {
            template.send(topic, msg).get();
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    /**
     * @Author: zmh
     * @Description: 同步 发送消息
     * @Date: 2020/5/9 14:54
     * @Version:1.0
     */
    public static boolean sendSync(String topic, String msg) {
        final boolean[] ok = {true};
        ListenableFuture<SendResult<String, String>> future = template.send(topic, msg);
        future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {

            @Override
            public void onFailure(@NonNull Throwable throwable) {
                log.error("sent message=[{}] failed!", msg, throwable);
                ok[0] = false;
            }

            @Override
            public void onSuccess(SendResult<String, String> result) {
                log.info("{}", result.getRecordMetadata());
                log.info("sent message=[{}] with offset=[{}] success!", msg, result.getRecordMetadata().offset());
            }
        });
        try {
            // 因为是异步发送，所以我们等待，最多5s
            future.get(5, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            log.error("waiting for kafka send finish failed!", e);
            return false;
        }
        return ok[0];
    }
}