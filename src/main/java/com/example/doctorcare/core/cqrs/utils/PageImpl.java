package com.example.doctorcare.core.cqrs.utils;

import java.util.List;

/**
 * Implementation cụ thể của interface Page
 */
public class PageImpl<T> implements Page<T> {
    
    private final List<T> items;
    private final long pageNumber;
    private final long pageSize;
    private final long totalElements;
    
    public PageImpl(List<T> items, long pageNumber, long pageSize, long totalElements) {
        this.items = items;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalElements = totalElements;
    }
    
    @Override
    public long getTotalPages() {
        return pageSize > 0 ? (long) Math.ceil((double) totalElements / pageSize) : 0;
    }
    
    @Override
    public long getTotalElements() {
        return totalElements;
    }
    
    @Override
    public long getPageSize() {
        return pageSize;
    }
    
    @Override
    public long getPageNumber() {
        return pageNumber;
    }
    
    @Override
    public List<T> getItems() {
        return items;
    }
}