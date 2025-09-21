package com.example.doctorcare.core.cqrs.utils;

import java.util.Collections;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

/**
 * Lớp triển khai cụ thể của interface {@link com.example.doctorcare.core.cqrs.utils.Page}.
 * <p>
 * Lớp này được sử dụng trong toàn bộ ứng dụng để đóng gói và trả về kết quả phân trang
 * từ các Query Handler. Nó cung cấp các phương thức factory tĩnh tiện lợi để tạo
 * các đối tượng Paging từ {@code org.springframework.data.domain.Page} hoặc từ các
 * danh sách thông thường.
 *
 * @param <T> Kiểu dữ liệu của các phần tử trong trang.
 */
public class Paging<T> implements com.example.doctorcare.core.cqrs.utils.Page<T> {
    /**
     * Tổng số trang.
     */
    private long totalPages;

    /**
     * Tổng số phần tử trên tất cả các trang.
     */
    private long totalElements;

    /**
     * Kích thước của trang (số phần tử trên một trang).
     */
    private long pageSize;

    /**
     * Số thứ tự của trang hiện tại (bắt đầu từ 0).
     */
    private long pageNumber;

    /**
     * Danh sách các phần tử của trang hiện tại.
     */
    private List<T> items;

    /**
     * Constructor riêng tư để tạo một trang chỉ với nội dung, không có thông tin phân trang.
     * @param dtos danh sách các mục.
     */
    private Paging(List<T> dtos) {
        this.items = dtos;
    }

    /**
     * Constructor riêng tư để tạo một đối tượng Paging từ một đối tượng {@code org.springframework.data.domain.Page}.
     * @param page đối tượng Page của Spring Data.
     */
    private Paging(Page<T> page) {
        this.totalPages = page.getTotalPages();
        this.totalElements = page.getTotalElements();
        this.pageSize = page.getSize();
        this.pageNumber = page.getNumber();
        this.items = page.getContent();
    }

    /**
     * Constructor riêng tư để tạo một đối tượng Paging từ một trang của một kiểu dữ liệu khác
     * (thường là entity) và một danh sách DTOs đã được ánh xạ.
     * @param entities trang nguồn chứa thông tin phân trang.
     * @param dtos danh sách các mục đã được chuyển đổi.
     */
    private Paging(Page<?> entities, List<T> dtos) {
        this.totalPages = entities.getTotalPages();
        this.totalElements = entities.getTotalElements();
        this.pageSize = entities.getPageable().getPageSize();
        this.pageNumber = entities.getPageable().getPageNumber();
        this.items = dtos;
    }

    /**
     * Tạo một đối tượng Paging chỉ chứa danh sách các mục, không có thông tin phân trang.
     * Hữu ích cho các kết quả không phân trang nhưng cần tuân thủ định dạng trả về.
     * @param dtos danh sách các mục.
     * @param <T> kiểu dữ liệu của các mục.
     * @return một đối tượng Paging.
     */
    public static <T> Paging<T> of(List<T> dtos) {
        return new Paging<>(dtos);
    }

    /**
     * Tạo một đối tượng Paging từ một đối tượng {@code org.springframework.data.domain.Page} của Spring Data.
     * Đây là cách sử dụng phổ biến nhất trong các Query Handler.
     * @param entities đối tượng Page của Spring Data.
     * @param <T> kiểu dữ liệu của các mục.
     * @return một đối tượng Paging.
     */
    public static <T> Paging<T> of(Page<T> entities) {
        return new Paging<>(entities);
    }

    /**
     * Tạo một đối tượng Paging bằng cách lấy thông tin phân trang từ một {@code Page} của kiểu khác
     * (thường là {@code Page<Entity>}) và nội dung từ một danh sách DTOs đã được ánh xạ.
     * @param entities trang nguồn chứa thông tin phân trang.
     * @param dtos danh sách các mục đã được chuyển đổi.
     * @param <T> kiểu dữ liệu của các mục.
     * @return một đối tượng Paging.
     */
    public static <T> Paging<T> of(Page<?> entities, List<T> dtos) {
        return new Paging<>(entities, dtos);
    }

    /**
     * Tạo một trang rỗng với thông tin phân trang được cung cấp.
     * Hữu ích khi một query không trả về kết quả nào.
     * @param <T>      Kiểu dữ liệu của các phần tử trong trang.
     * @param pageable Thông tin phân trang (số trang, kích thước trang).
     * @return một đối tượng Paging rỗng.
     */
    public static <T> Paging<T> empty(Pageable pageable) {
        // Sử dụng PageImpl của Spring để tạo một trang rỗng với tổng số phần tử là 0.
        Page<T> emptySpringPage = new PageImpl<>(Collections.emptyList(), pageable, 0);
        // Sử dụng constructor đã có để tạo đối tượng Paging từ Spring Page.
        return new Paging<>(emptySpringPage);
    }

    public static <T> Paging<T> of(List<T> dtos, Pageable pageable, long totalElements) {
        Page<T> page = new PageImpl<>(dtos, pageable, totalElements);
        return new Paging<>(page);
    }

    @Override
    public long getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(long totalPages) {
        this.totalPages = totalPages;
    }

    @Override
    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    @Override
    public long getPageSize() {
        return pageSize;
    }

    public void setPageSize(long pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public long getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(long pageNumber) {
        this.pageNumber = pageNumber;
    }

    @Override
    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

}