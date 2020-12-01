package sg.storage.init;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.net.Inet4Address;

@Slf4j
@Component
public class StartupLoading implements CommandLineRunner {

    @Value("${server.port}")
    private String port;
    @Value("${server.servlet.context-path}")
    private String server;

    @Override
    public void run(String... strings) throws Exception {
        log.info("Spring Boot启动");
        String localHost = Inet4Address.getLocalHost().getHostAddress();
        log.info("http://" + localHost + ":" + port + server);
    }
}