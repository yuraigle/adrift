package ru.orlov.adrift.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.nio.file.Path;

@RequiredArgsConstructor
@Configuration
public class AppConfig {

    private final Environment env;

    @Bean(name = "appPath")
    String appPath() {
        String appPath = env.getProperty("APP_PATH", ".");
        appPath = Path.of(appPath).toString();

        return appPath;
    }

    @Bean(name = "webappDist")
    String webappDist() {
        return Path.of(appPath(), "webapp", "dist").toString();
    }

}
