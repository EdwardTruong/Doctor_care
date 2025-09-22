package com.example.doctorcare.domain.system.role;

    
/**
 * Phân loại vai trò để xác định ngữ cảnh áp dụng chính của chúng. 
 * Thuận tiện cho Frontend filter các combobox vai trò ở các ngữ cảnh khác nhau
 * 
 */
public enum RoleType {

    /**
     * Vai trò người dùng chủ yếu là bệnh nhân hoặc là người đặc lịch khám.
     */
    ROLE_USER,
	
	/**
     * Vai trò người khám bệnh. 
     * Ví dụ : Bác sỹ, Bác sỹ đầu ngành, Bác sỹ trưởng khoa, Bác sỹ chuyên khoa ..v.v..
     */
	ROLE_DOCTOR,
	
	
    /**
     * Vai trò liên quan đến một người đầu tư đơn lẽ.
     * Ví dụ: Vai trò người đầu tư hoặc là người hỗ trỡ người đầu tư.
     */
    INDIVIDUAL,

    /**
     * Vai trò được sử dụng trong phạm vi một nhóm đầu.
     * Ví dụ: Quản trị viên của 1 doanh nghiệp đầu tư, hoặc là người 1 tham gia vào thành 1 nhóm.
     */
    INSTITUTIONAL,

    /**
     * Vai trò được sử dụng trong phạm vi một Tổ chức (hiệp hội, CLB...).
     * Ví dụ: Quản trị viên Tổ chức, Quản lý Hội viên.
     */
    ORGANIZATION,

    /**
     * Vai trò có phạm vi toàn cục, không bị giới hạn bởi một doanh nghiệp hay tổ chức cụ thể.
     * Ví dụ: Quản trị viên Hệ thống (SUPER_ADMIN).
     */
    GLOBAL

}
