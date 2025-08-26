package ru.orlov.adrift.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.nio.file.Path;

@Log4j2
@RequiredArgsConstructor
@Configuration
public class AppConfig {

    private final Environment env;

    @Bean(name = "appPath")
    String appPath() {
        String appPath = env.getProperty("APP_PATH", ".");
        appPath = appPath.trim();
        appPath = Path.of(appPath).toString();
        log.info("APP_PATH = {}", appPath);

        return appPath;
    }

}
