package ru.orlov.adrift.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.*;

import java.nio.file.Path;

@EnableWebMvc
@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final Environment env;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "HEAD");
    }

    @Override
    public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
        String appPath = env.getProperty("APP_PATH", ".");
        appPath = appPath.replace('\\', '/').trim();
        String webappDist = Path.of(appPath, "webapp") + "/";
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
