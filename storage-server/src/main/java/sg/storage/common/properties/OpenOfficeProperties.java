package sg.storage.common.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * OpenOffice
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "open-office")
public class OpenOfficeProperties {

    /**
     * ip
     */
    private String ip;
    /**
     * 端口
     */
    private Integer port;
}