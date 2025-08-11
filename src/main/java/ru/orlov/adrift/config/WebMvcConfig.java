package ru.orlov.adrift.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.*;

import java.nio.file.Path;
import java.util.List;

@Log4j2
@EnableWebMvc
@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final Environment env;

    @Override
    public void addCorsMappings(@NonNull CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE");
    }

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
    public void addViewControllers(@NonNull ViewControllerRegistry registry) {
        List<String> ssrRoutes = List.of(
                "/", "/about",
                "/auth/login", "/auth/register"
        );

        ssrRoutes.forEach(route ->
                registry.addViewController(route)
                        .setViewName("forward:" + route + "/index.html")
        );
    }
}
