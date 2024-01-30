package ru.clevertec.houses.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                description = "The project is a task for using SpringBoot",
                title = "House-Api Documentation",
                termsOfService = "Term of service"
        ),
        servers = @Server(
                description = "Local",
                url = "http://localhost:8081"
        ))
public class SwaggerConfig {

}
