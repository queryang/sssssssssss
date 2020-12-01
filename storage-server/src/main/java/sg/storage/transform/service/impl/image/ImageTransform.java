package sg.storage.transform.service.impl.image;

import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Component;
import sg.storage.common.properties.TransformFileSizeProperties;
import sg.storage.transform.service.ITransformService;
import sg.storage.model.vo.file.TransformRequest;

import java.io.IOException;

/**
 * 图片转化
 */
@Slf4j
@Component
public class ImageTransform implements ITransformService {

    @Override
    public void transform(TransformRequest transformRequest) throws Exception {
        TransformFileSizeProperties.Size size = TransformFileSizeProperties.getImageSize(transformRequest.getTransformFileSizeType());
        if (null == size) {
            transcoding(transformRequest.getOriginFilePath()
                    , transformRequest.getTransformFileSuffix()
                    , transformRequest.getTransformFilePath());
        } else {
            transcoding(transformRequest.getOriginFilePath()
                    , transformRequest.getTransformFileSuffix()
                    , transformRequest.getTransformFilePath()
                    , size.getWidth().intValue()
                    , size.getHeight().intValue());
        }
    }

    /**
     * 按源文件尺寸转化
     *
     * @param originFilePath      源文件路径
     * @param transformFileSuffix 转化文件后缀
     * @param transformFilePath   转化文件路径
     * @throws IOException
     */
    public static void transcoding(String originFilePath, String transformFileSuffix, String transformFilePath) throws Exception {
        Thumbnails.of(originFilePath).scale(1f).outputFormat(transformFileSuffix).toFile(transformFilePath);
    }

    /**
     * 按自定义尺寸转化
     *
     * @param originFilePath      源文件路径
     * @param transformFileSuffix 转化文件后缀
     * @param transformFilePath   转化文件路径
     * @param width               宽
     * @param height              高
     * @throws Exception
     */
    public static void transcoding(String originFilePath, String transformFileSuffix, String transformFilePath, int width, int height) throws Exception {
        Thumbnails.of(originFilePath).forceSize(width, height).outputFormat(transformFileSuffix).toFile(transformFilePath);
    }
}