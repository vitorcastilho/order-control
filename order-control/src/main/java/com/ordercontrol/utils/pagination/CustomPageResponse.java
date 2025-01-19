package com.ordercontrol.utils.pagination;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;

public class CustomPageResponse<T> {

	private ResultSetMetadata resultSetMetadata;
	private List<T> results;

	public CustomPageResponse(long count, long offset, int limit, List<T> results) {
		this.resultSetMetadata = new ResultSetMetadata(count, offset, limit);
		this.results = results;
	}

	public static <T, R> CustomPageResponse<R> fromPage(Page<T> page, Function<T, R> mapper) {
		long count = page.getTotalElements();
		long offset = page.getPageable().getOffset();
		int limit = page.getPageable().getPageSize();
		List<R> results = page.getContent().stream().map(mapper).collect(Collectors.toList());
		return new CustomPageResponse<>(count, offset, limit, results);
	}

	public static <T> CustomPageResponse<T> fromPage(Page<T> page) {
		long count = page.getTotalElements();
		long offset = page.getPageable().getOffset();
		int limit = page.getPageable().getPageSize();
		List<T> results = page.getContent();
		return new CustomPageResponse<>(count, offset, limit, results);
	}

	public ResultSetMetadata getResultSetMetadata() {
		return resultSetMetadata;
	}

	public void setResultSetMetadata(ResultSetMetadata resultSetMetadata) {
		this.resultSetMetadata = resultSetMetadata;
	}

	public List<T> getResults() {
		return results;
	}

	public void setResults(List<T> results) {
		this.results = results;
	}

	public static class ResultSetMetadata {
		private long count;
		private long offset;
		private int limit;

		public ResultSetMetadata(long count, long offset, int limit) {
			this.count = count;
			this.offset = offset;
			this.limit = limit;
		}

		public long getCount() {
			return count;
		}

		public void setCount(long count) {
			this.count = count;
		}

		public long getOffset() {
			return offset;
		}

		public void setOffset(long offset) {
			this.offset = offset;
		}

		public int getLimit() {
			return limit;
		}

		public void setLimit(int limit) {
			this.limit = limit;
		}
	}
}
