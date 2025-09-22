package com.example.doctorcare.application.exception;

/**
 * Exception được ném ra khi cố gắng tạo một vai trò với tên đã tồn tại.
 * Kế thừa từ {@link ConflictException} để trả về HTTP status 409 Conflict.
 */
public class RoleAlreadyExistsException extends ConflictException {
    public RoleAlreadyExistsException(String roleName) {
        super("Vai trò với tên '" + roleName + "' đã tồn tại.", "error.role.alreadyExists", roleName);
    }
}