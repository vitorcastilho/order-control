package com.ordercontrol.infrastructure.configuration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.function.Predicate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;

import com.ordercontrol.web.api.v1.controller.CustomerController;
import com.ordercontrol.web.api.v1.controller.CustomerOrderController;
import com.ordercontrol.web.api.v1.controller.ProductController;

@DisplayName("ApiVersionConfig")
class ApiVersionConfigTest {

	@Test
	@DisplayName("registra o prefixo api/v1 apenas para os controllers do pacote versionado")
	void registersPrefixForVersionedControllersOnly() {
		PathMatchConfigurer configurer = mock(PathMatchConfigurer.class);
		ArgumentCaptor<String> prefix = ArgumentCaptor.forClass(String.class);
		@SuppressWarnings("unchecked")
		ArgumentCaptor<Predicate<Class<?>>> predicate = ArgumentCaptor.forClass(Predicate.class);

		new ApiVersionConfig().configurePathMatch(configurer);

		verify(configurer).addPathPrefix(prefix.capture(), predicate.capture());
		assertEquals("api/v1", prefix.getValue());

		Predicate<Class<?>> appliesTo = predicate.getValue();
		assertTrue(appliesTo.test(CustomerController.class));
		assertTrue(appliesTo.test(ProductController.class));
		assertTrue(appliesTo.test(CustomerOrderController.class));
		assertFalse(appliesTo.test(ApiVersionConfig.class));
		assertFalse(appliesTo.test(String.class));
	}
}
