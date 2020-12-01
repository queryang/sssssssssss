package sg.storage.common.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 阿里OSS存储
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "oss")
public class AliOSSProperties {

    /**
     * 阿里云oss站点
     */
    private String endpoint;
    /**
     * 阿里云oss资源访问url
     */
    private String url;
    /**
     * 阿里云oss公钥
     */
    private String accessKeyId;
    /**
     * 阿里云oss私钥
     */
    private String accessKeySecret;
    /**
     * 阿里云oss文件根目录
     */
    private String bucketName;
}