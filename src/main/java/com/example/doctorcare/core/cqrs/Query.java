package com.example.doctorcare.core.cqrs;

/**
 * Một interface đại diện cho một Query trong mẫu CQRS.
 * <p>
 * Một Query là một đối tượng mang ý định truy vấn dữ liệu từ hệ thống.
 * Nó không được phép thay đổi trạng thái của hệ thống.
 *
 * @param <R> Kiểu dữ liệu trả về của Query.
 */
public interface Query<R> {}