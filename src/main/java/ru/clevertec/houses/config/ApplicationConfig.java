package ru.clevertec.houses.config;

import ru.clevertec.houses.config.util.YamlPropertySourceFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan("ru.clevertec.houses.*")
@PropertySource(factory = YamlPropertySourceFactory.class, value = "classpath:application.yaml")
public class ApplicationConfig {

}
