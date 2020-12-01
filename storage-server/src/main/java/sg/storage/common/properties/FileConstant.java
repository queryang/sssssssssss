package sg.storage.common.properties;

import java.util.ArrayList;
import java.util.List;

/**
 * 文件常量
 */
public class FileConstant {

    /**
     * ==========文件存储位置==========
     */
    /**
     * 本地存储
     */
    public final static String LOCAL = "local";
    /**
     * 阿里OSS存储
     */
    public final static String OSS = "oss";

    /**
     * ==========文件尺寸类型==========
     */
    /**
     * 原文件
     */
    public final static String ORIGINAL = "o";
    /**
     * 缩略图（图片与视频封面）
     */
    public final static String THUMBNAIL = "t";
    /**
     * 小尺寸图(图片与视频封面)
     */
    public final static String SMALL_SIZE = "s";
    /**
     * 中尺寸图(图片与视频封面)
     */
    public final static String MIDDLE_SIZE = "m";
    /**
     * 大尺寸图(图片与视频封面)
     */
    public final static String LARGE_SIZE = "l";
    /**
     * image类型文件尺寸
     */
    public final static List<String> IMAGE_SIZES = new ArrayList<String>(5) {
        {
            this.add(ORIGINAL);
            this.add(THUMBNAIL);
            this.add(SMALL_SIZE);
            this.add(MIDDLE_SIZE);
            this.add(LARGE_SIZE);
        }
    };
    /**
     * video类型文件尺寸
     */
    public final static List<String> VIDEO_SIZES = new ArrayList<String>(4) {
        {
            this.add(THUMBNAIL);
            this.add(SMALL_SIZE);
            this.add(MIDDLE_SIZE);
            this.add(LARGE_SIZE);
        }
    };
}