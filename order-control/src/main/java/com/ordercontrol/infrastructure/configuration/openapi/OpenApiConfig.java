package com.ordercontrol.infrastructure.configuration.openapi;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenApiConfig {

	@Bean
	OpenAPI orderControlOpenApi() {
		return new OpenAPI()
				.info(new Info()
						.title("Order Control API")
						.version("v1")
						.description("""
								API de gerenciamento de pedidos.

								Os pedidos dão baixa no estoque do produto no momento da criação; \
								a operação é recusada quando o saldo é insuficiente. \
								A consulta por id é servida a partir do cache Redis, invalidado \
								a cada alteração de status.""")
						.contact(new Contact().name("Vitor Castilho").url("https://github.com/vitorcastilho"))
						.license(new License().name("MIT").url("https://opensource.org/licenses/MIT")))
				.servers(List.of(new Server().url("http://localhost:8080").description("Ambiente local")));
	}
}
