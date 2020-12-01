package sg.storage.transform.service;

import sg.storage.model.vo.file.TransformRequest;

/**
 * 转化文件
 */
public interface ITransformService {

    /**
     * 转化文件
     *
     * @param transformRequest 转化请求
     */
    void transform(TransformRequest transformRequest) throws Exception;
}