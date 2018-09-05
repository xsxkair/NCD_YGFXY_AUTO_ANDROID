package com.ncd.xsx.ncd_ygfxy.Databases;

import java.util.List;

public class Page<T> {

	private Long currentPageIndex;

	private Long totalPages;
	
	private Long totalElements;
	
	private List<T> content;

	public Page() {
		super();
	}

	public Page(Long currentPageIndex, Long totalPages, Long totalElements, List<T> content) {
		super();
		this.currentPageIndex = currentPageIndex;
		this.totalPages = totalPages;
		this.totalElements = totalElements;
		this.content = content;
	}

	public Long getCurrentPageIndex() {
		return currentPageIndex+1;
	}

	public void setCurrentPageIndex(Long currentPageIndex) {
		this.currentPageIndex = currentPageIndex;
	}

	public Long getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(Long totalPages) {
		this.totalPages = totalPages;
	}

	public Long getTotalElements() {
		return totalElements;
	}

	public void setTotalElements(Long totalElements) {
		this.totalElements = totalElements;
	}

	public List<T> getContent() {
		return content;
	}

	public void setContent(List<T> content) {
		this.content = content;
	}
	
	
}
