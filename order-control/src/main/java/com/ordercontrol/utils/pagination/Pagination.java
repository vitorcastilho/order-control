package com.ordercontrol.utils.pagination;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import jakarta.validation.constraints.Min;

public class Pagination {

	@Min(value = 0, message = "Offset must be greater than or equal to 0")
	private long offset = 0;

	@Min(value = 1, message = "Limit must be greater than or equal to 1")
	private int limit = 10;

	private String sortBy = "id";

	public Pagination() {
	}

	public Pagination(long offset, int limit, String sortBy) {
		this.offset = offset;
		this.limit = limit;
		this.sortBy = sortBy;
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

	public String getSortBy() {
		return sortBy;
	}

	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}

	public Pageable toPageable() {
		int page = (int) (offset / limit);
		return PageRequest.of(page, limit, Sort.by(sortBy));
	}
}
