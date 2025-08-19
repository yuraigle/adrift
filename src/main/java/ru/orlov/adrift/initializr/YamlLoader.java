package ru.orlov.adrift.initializr;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class YamlLoader {

    @Bean
    ObjectMapper objectMapper(YAMLFactory yamlFactory) {
        return new ObjectMapper(yamlFactory);
    }

    @Bean
    YAMLFactory yamlFactory() {
        return new YAMLFactory();
    }

}
