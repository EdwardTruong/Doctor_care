package com.example.doctorcare.core.cqrs;

/**
 * Một marker interface đại diện cho một Command trong mẫu CQRS.
 * <p>
 * Một Command là một đối tượng mang ý định thay đổi trạng thái của hệ thống
 * và thường không trả về dữ liệu. Đối với các command cần trả về kết quả,
 * hãy sử dụng {@link CommandWithResult}.
 */
public interface Command {}