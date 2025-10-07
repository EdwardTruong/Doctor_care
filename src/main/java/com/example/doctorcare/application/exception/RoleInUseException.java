package com.example.doctorcare.application.exception;

import java.io.Serial;


/**
 * Exception class named {@link RoleInUseException} thrown when the specified
 * role is not found.
 */
public class RoleInUseException extends RuntimeException {

	@Serial
	private static final long serialVersionUID = -6123426931314984189L;


	/**
	 * Constructs a new {@link RoleInUseException} with the default message.
	 */
	public RoleInUseException() {
		super();
	}

	/**
	 * Constructs a new {@link RoleInUseException} with the default message and
	 * an additional message.
	 *
     * @param roleName name of role 
	 * @param reason the additional message to include.
	 */
    public RoleInUseException(String roleName, String reason) {
        super("Không thể xóa vai trò '" + roleName + "'. Lý do: " + reason);
    }

}
