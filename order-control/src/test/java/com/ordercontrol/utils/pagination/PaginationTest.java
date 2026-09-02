package com.ordercontrol.utils.pagination;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@DisplayName("Pagination")
class PaginationTest {

	@Test
	@DisplayName("usa offset 0, limite 10 e ordenação por id como padrão")
	void defaults() {
		Pagination pagination = new Pagination();

		assertEquals(0, pagination.getOffset());
		assertEquals(10, pagination.getLimit());
		assertEquals("id", pagination.getSortBy());
	}

	@ParameterizedTest(name = "offset {0} com limite {1} vira a página {2}")
	@CsvSource({ "0, 10, 0", "10, 10, 1", "50, 10, 5", "9, 10, 0", "20, 5, 4" })
	@DisplayName("converte offset em número de página pela divisão inteira")
	void translatesOffsetIntoPageNumber(long offset, int limit, int expectedPage) {
		Pageable pageable = new Pagination(offset, limit, "id").toPageable();

		assertEquals(expectedPage, pageable.getPageNumber());
		assertEquals(limit, pageable.getPageSize());
	}

	@Test
	@DisplayName("aplica a ordenação informada")
	void appliesSort() {
		Pageable pageable = new Pagination(0, 10, "name").toPageable();

		assertEquals(Sort.by("name"), pageable.getSort());
	}

	@Test
	@DisplayName("expõe os valores atribuídos pelos setters")
	void accessors() {
		Pagination pagination = new Pagination();
		pagination.setOffset(30);
		pagination.setLimit(15);
		pagination.setSortBy("email");

		assertEquals(30, pagination.getOffset());
		assertEquals(15, pagination.getLimit());
		assertEquals("email", pagination.getSortBy());
	}
}
