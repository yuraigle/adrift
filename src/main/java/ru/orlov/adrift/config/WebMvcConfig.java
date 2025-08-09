package ru.orlov.adrift.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;

@EnableWebMvc
@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final Environment env;

    @Override
    public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
        String appPath = env.getProperty("APP_PATH", ".");
        appPath = appPath.replace('\\', '/').trim();
        String webappDist = Path.of(appPath, "webapp/dist") + "/";
        webappDist = webappDist.replace("//", "/");

        if (!webappDist.startsWith("file:/")) {
            webappDist = "file:/" + webappDist;
        }

        registry.addResourceHandler("/**")
                .addResourceLocations(webappDist);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("forward:/index.html");
    }
}
