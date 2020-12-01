package sg.storage.transform.event;

import sg.storage.model.vo.file.TransformRequest;
import sg.storage.transform.center.TransformCenter;

import java.util.concurrent.LinkedTransferQueue;

/**
 * 转化队列
 */
public class TransformQueue {

    private static final TransformQueue TRANSFORMQUEUE = new TransformQueue();
    private static final LinkedTransferQueue<TransformRequest> LINKEDTRANSFERQUEUE = new LinkedTransferQueue<>();
    /**
     * 是否转化中
     */
    private static boolean transforming = false;

    private TransformQueue() {
    }

    public static TransformQueue getInstance() {
        return TRANSFORMQUEUE;
    }

    /**
     * 将转化请求追加进队列
     *
     * @param transformRequest 转化请求
     * @return
     */
    public synchronized boolean addQueue(TransformRequest transformRequest) {
        if (null == transformRequest) {
            return false;
        }
        boolean flag = LINKEDTRANSFERQUEUE.add(transformRequest);
        if (flag && LINKEDTRANSFERQUEUE.size() > 0 && !transforming) {
            transforming = true;
        }
        return flag;
    }

    /**
     * 转化队列
     */
    public void transformQueue() {
        while (LINKEDTRANSFERQUEUE.size() > 0) {
            TransformRequest transformRequest = LINKEDTRANSFERQUEUE.poll();
            TransformCenter.transform(transformRequest);
        }
        release();
    }

    /**
     * 释放是否转化中
     */
    public synchronized void release() {
        transforming = false;
    }
}