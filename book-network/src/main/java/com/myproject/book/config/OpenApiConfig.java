package com.myproject.book.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Book Network",
                        email = "booknetwork@gmail.com",
                        url = "https://booknetwork.com"
                ),
                description = "OpenAPI documentation for Book Network API",
                title = "OpenApi Specification for Book Network API",
                version = "1.0",
                license = @License(name = "Book Network License", url = "https://booknetwork.com/license"),
                termsOfService = "https://booknetwork.com/terms"

        ),
        servers = {
                @Server(url = "http://localhost:8088/api/v1", description = "Local server"),
                @Server(url = "https://booknetwork.com", description = "Production server")
        },
        security = {
                @SecurityRequirement(name = "bearerAuth")
        }
)
@SecurityScheme(
        name = "bearerAuth",
        description = "JWT auth description",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
}
// accessible at
// http://localhost:8088/api/v1/swagger-ui/index.html