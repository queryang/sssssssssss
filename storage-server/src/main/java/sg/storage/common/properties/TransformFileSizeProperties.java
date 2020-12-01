package sg.storage.common.properties;

import lombok.Getter;
import lombok.Setter;

/**
 * 转化文件尺寸
 */
public class TransformFileSizeProperties {

    /**
     * 图片缩略图
     */
    private final static Size IMAGE_THUMBNAIL_SIZE = new Size(200D, 130D);
    /**
     * 图片小尺寸
     */
    private final static Size IMAGE_SMALL_SIZE = new Size(640D, 480D);
    /**
     * 图片中尺寸
     */
    private final static Size IMAGE_MIDDLE_SIZE = new Size(1024D, 768D);
    /**
     * 图片大尺寸
     */
    private final static Size IMAGE_LARGE_SIZE = new Size(1920D, 1080D);
    /**
     * 视频小尺寸
     */
    private final static Size VOIDE_SMALL_SIZE = new Size(320D, 240D);
    /**
     * 视频中尺寸
     */
    private final static Size VOIDE_MIDDLE_SIZE = new Size(720D, 480D);
    /**
     * 视频大尺寸
     */
    private final static Size VOIDE_LARGE_SIZE = new Size(800D, 480D);

    /**
     * 获取图片尺寸
     *
     * @param fileSizeType
     * @return
     */
    public static Size getImageSize(String fileSizeType) {
        switch (fileSizeType) {
            case FileConstant.THUMBNAIL:
                return TransformFileSizeProperties.IMAGE_THUMBNAIL_SIZE;
            case FileConstant.LARGE_SIZE:
                return TransformFileSizeProperties.IMAGE_LARGE_SIZE;
            case FileConstant.MIDDLE_SIZE:
                return TransformFileSizeProperties.IMAGE_MIDDLE_SIZE;
            case FileConstant.SMALL_SIZE:
                return TransformFileSizeProperties.IMAGE_SMALL_SIZE;
        }
        return null;
    }

    /**
     * 获取视频尺寸
     *
     * @param fileSizeType
     * @return
     */
    public static Size getVoideSize(String fileSizeType) {
        switch (fileSizeType) {
            case FileConstant.THUMBNAIL:
                return TransformFileSizeProperties.IMAGE_THUMBNAIL_SIZE;
            case FileConstant.LARGE_SIZE:
                return TransformFileSizeProperties.VOIDE_LARGE_SIZE;
            case FileConstant.MIDDLE_SIZE:
                return TransformFileSizeProperties.VOIDE_MIDDLE_SIZE;
            case FileConstant.SMALL_SIZE:
                return TransformFileSizeProperties.VOIDE_SMALL_SIZE;
        }
        return null;
    }

    @Getter
    @Setter
    public static class Size {

        private Double width;
        private Double height;

        public Size(Double width, Double height) {
            this.width = width;
            this.height = height;
        }

        @Override
        public String toString() {
            return "Size{"
                    + "width=" + width
                    + ", height=" + height
                    + "}";
        }
    }
}