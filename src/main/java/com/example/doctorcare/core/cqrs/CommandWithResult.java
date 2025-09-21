package com.example.doctorcare.core.cqrs;

/**
 * Một interface đại diện cho một Command có trả về kết quả trong mẫu CQRS.
 * <p>
 * Khác với {@link Command} thông thường, Command này được thiết kế để trả về
 * một kết quả sau khi thực thi, thường là DTO của tài nguyên vừa được tạo hoặc cập nhật.
 *
 * @param <R> Kiểu dữ liệu trả về của Command.
 */
public interface CommandWithResult<R> {}