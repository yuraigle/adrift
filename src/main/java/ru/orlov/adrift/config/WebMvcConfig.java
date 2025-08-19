package ru.orlov.adrift.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.*;

import java.util.List;

@Log4j2
@EnableWebMvc
@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final String webappDist;

    @Override
    public void addCorsMappings(@NonNull CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE");
    }

    @Override
    public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("file:/" + webappDist);
    }

    @Override
    public void addViewControllers(@NonNull ViewControllerRegistry registry) {
        List<String> ssrRoutes = List.of(
                "/", "/about",
                "/auth/login", "/auth/register"
        );

        for (String route : ssrRoutes) {
            String html = route.replace("/", "") + "/index.html";
            registry.addViewController(route)
                    .setViewName("forward:" + html);
        }
    }
}
