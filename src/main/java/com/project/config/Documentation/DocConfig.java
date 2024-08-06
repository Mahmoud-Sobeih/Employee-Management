package com.project.config.Documentation;

import java.util.List;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class DocConfig {

    @Bean
    public OpenAPI myOpenAPI() {
        Server devServer = new Server();
        String devUrl = "http://localhost:8080";
        devServer.setUrl(devUrl);
        devServer.setDescription("Server URL in Development environment");

        Server prodServer = new Server();
        String prodUrl = "https://production-api.com";
        prodServer.setUrl(prodUrl);
        prodServer.setDescription("Server URL in Production environment");

        Contact contact = new Contact();
        contact.setEmail("bezkoder@gmail.com");
        contact.setName("BezKoder");
        contact.setUrl("https://www.bezkoder.com");

        License mitLicense = new License().name("MIT License").url("https://choosealicense.com/licenses/mit/");

        Info info = new Info()
                .title("Company Management API")
                .version("1.0")
                .contact(contact)
                .description("This API exposes endpoints to manage departments and employees.").termsOfService("https://www.bezkoder.com/terms")
                .license(mitLicense);

        return new OpenAPI().info(info).servers(List.of(devServer, prodServer));
    }

}
