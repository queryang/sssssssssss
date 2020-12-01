package sg.storage.common.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import sg.storage.common.properties.FileProperties;

/**
 * WebMvc配置
 */
@Configuration
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private FileProperties fileConfiguration;

    /**
     * 静态资源配置
     *
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
        String path = fileConfiguration.getBasePath();
        if (!path.substring(path.length() - 1).contains("/")
                || !path.substring(path.length() - 1).contains("\\")) {
            if (path.contains("/")) {
                path = path.concat("/");
            } else {
                path = path.concat("\\");
            }
        }
        registry.addResourceHandler(path + "**")
                .addResourceLocations("file:" + path);

    }
}