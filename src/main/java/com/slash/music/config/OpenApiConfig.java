package com.slash.music.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@Configuration
@OpenAPIDefinition(
  info = @Info(
    title = "Slash Music API",
    description = "REST API for Slash Music streaming application",
    version = "1.0.0",
    license = @License(
      name = "MIT License",
      url = "https://opensource.org/licenses/MIT"
    )
  ),
  servers = {
    @Server(url = "/", description = "Current Server")
  }
)
@SecurityScheme(
  name = "bearerAuth",
  type = SecuritySchemeType.HTTP,
  bearerFormat = "JWT",
  scheme = "bearer",
  description = "JWT Authorization header using the Bearer scheme. Example: \"Authorization: Bearer {token}\""
)
public class OpenApiConfig { }