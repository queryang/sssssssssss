package sg.storage.common.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 文件夹类型
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "folder")
public class FolderTypeProperties {

    /**
     * 文件夹类型
     */
    private List<Type> types;

    @Getter
    @Setter
    public static class Type {
        private Integer id;
        private String name;
    }

    /**
     * 依据id获取文件夹类型名称
     *
     * @param id
     * @return
     */
    public String getTypeNameById(Integer id) {
        if (null == id) {
            return null;
        }
        for (Type type : types) {
            if (id.equals(type.getId())) {
                return type.getName();
            }
        }
        return null;
    }
}