package com.ordercontrol.utils.pagination;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.ordercontrol.utils.pagination.CustomPageResponse.ResultSetMetadata;

@DisplayName("CustomPageResponse")
class CustomPageResponseTest {

	private Page<String> page(int pageNumber, int size, List<String> content, long total) {
		return new PageImpl<>(content, PageRequest.of(pageNumber, size), total);
	}

	@Test
	@DisplayName("fromPage sem mapper preserva o conteúdo e os metadados da página")
	void fromPageKeepsContent() {
		CustomPageResponse<String> response = CustomPageResponse
				.fromPage(page(1, 2, List.of("a", "b"), 7));

		assertEquals(List.of("a", "b"), response.getResults());
		assertEquals(7, response.getResultSetMetadata().getCount());
		assertEquals(2, response.getResultSetMetadata().getOffset());
		assertEquals(2, response.getResultSetMetadata().getLimit());
	}

	@Test
	@DisplayName("fromPage com mapper aplica a transformação a cada elemento")
	void fromPageAppliesMapper() {
		CustomPageResponse<Integer> response = CustomPageResponse
				.fromPage(page(0, 10, List.of("um", "dois"), 2), String::length);

		assertEquals(List.of(2, 4), response.getResults());
		assertEquals(2, response.getResultSetMetadata().getCount());
		assertEquals(0, response.getResultSetMetadata().getOffset());
	}

	@Test
	@DisplayName("página vazia resulta em lista vazia com contagem zero")
	void emptyPage() {
		CustomPageResponse<String> response = CustomPageResponse.fromPage(page(0, 10, List.of(), 0));

		assertEquals(List.of(), response.getResults());
		assertEquals(0, response.getResultSetMetadata().getCount());
	}

	@Test
	@DisplayName("expõe os valores atribuídos pelos setters")
	void accessors() {
		CustomPageResponse<String> response = new CustomPageResponse<>(1, 0, 10, List.of("x"));
		response.setResults(List.of("y", "z"));
		response.setResultSetMetadata(new ResultSetMetadata(99, 5, 25));

		assertEquals(List.of("y", "z"), response.getResults());
		assertEquals(99, response.getResultSetMetadata().getCount());
		assertEquals(5, response.getResultSetMetadata().getOffset());
		assertEquals(25, response.getResultSetMetadata().getLimit());

		ResultSetMetadata metadata = response.getResultSetMetadata();
		metadata.setCount(1);
		metadata.setOffset(2);
		metadata.setLimit(3);
		assertEquals(1, metadata.getCount());
		assertEquals(2, metadata.getOffset());
		assertEquals(3, metadata.getLimit());
	}
}
