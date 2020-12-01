package sg.storage.common.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 文件
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "file")
public class FileProperties {

    /**
     * 存储位置（local/oss）
     */
    private String location;
    /**
     * 基础路径
     */
    private String basePath;
    /**
     * 各类型文件转化信息
     */
    private List<TransformInfo> transformInfos;

    @Getter
    @Setter
    public static class TransformInfo {
        /**
         * 文件类型
         */
        private String fileType;
        /**
         * 是否自动转化
         */
        private Boolean autoTransform;
        /**
         * 需转化类型
         */
        private List<TransformType> transformTypes;

        @Getter
        @Setter
        public static class TransformType {
            /**
             * 转化文件后缀
             */
            private String transformSuffix;
            /**
             * 转化请求组装类
             */
            private String transformRequestClass;
            /**
             * 转化实现类
             */
            private String transformClass;
        }
    }

    /**
     * 依据文件类型获取是否自动转化
     *
     * @param fileType
     * @return
     */
    public Boolean getAutoTransform(String fileType) {
        for (TransformInfo transformInfo : transformInfos) {
            if (transformInfo.getFileType().equals(fileType)) {
                return transformInfo.autoTransform;
            }
        }
        return false;
    }

    /**
     * 依据文件类型获取文件转化信息
     *
     * @param fileType
     * @return
     */
    public TransformInfo getTransformInfo(String fileType) {
        for (TransformInfo transformInfo : transformInfos) {
            if ("other".equals(transformInfo.getFileType())) {
                return transformInfo;
            }
            if (fileType.equals(transformInfo.getFileType())) {
                return transformInfo;
            }
        }
        return null;
    }

    /**
     * 依据文件类型获取需转化类型
     *
     * @param fileType
     * @return
     */
    public List<TransformInfo.TransformType> getTransformTypes(String fileType) {
        for (TransformInfo transformInfo : transformInfos) {
            if (transformInfo.getFileType().equals(fileType)) {
                return transformInfo.getTransformTypes();
            }
        }
        return null;
    }
}