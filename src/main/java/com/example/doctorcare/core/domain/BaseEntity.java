package com.example.doctorcare.core.domain;

import java.time.Instant;
import java.time.LocalDateTime;

import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * Lớp cơ sở trừu tượng cho tất cả các thực thể (entity) trong hệ thống.
 * <p>
 * Lớp này cung cấp các trường và chức năng chung, bao gồm:
 * <ul>
 *     <li><b>Kiểm toán (Auditing):</b> Tự động theo dõi người tạo, ngày tạo, người sửa đổi cuối cùng,
 *     và ngày sửa đổi cuối cùng. Được kích hoạt bởi {@link EntityListeners} và
 *     {@link com.example.doctorcare.core.security.SecurityAuditorAware}.</li>
 *     <li><b>Xóa mềm (Soft Delete):</b> Cung cấp một cờ {@code deleted} và các trường liên quan
 *     để đánh dấu một bản ghi đã bị xóa thay vì xóa vĩnh viễn khỏi cơ sở dữ liệu.</li>
 * </ul>
 * Việc sử dụng {@link MappedSuperclass} cho phép các lớp con kế thừa các trường này
 * như thể chúng được định nghĩa trực tiếp trong lớp con.
 * </p> <br>
 * <p>Chức năng hẹ giờ cũng sẽ được tận dụng ở đây.</p>
 * @param <I> Kiểu dữ liệu của định danh (ID) của thực thể.
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseEntity<I> implements AuditableEntity {
    
	public abstract I getId();

	public abstract void setId(I id);
	
    /**
     * Tên đăng nhập của người dùng đã tạo thực thể.
     * Được tự động điền bởi Spring Data JPA Auditing.
     */
	@CreatedBy
    @Column(name = "created_by", nullable = false, length = 50, updatable = false)
    private String createdBy;

    /**
     * Thời điểm (timestamp) thực thể được tạo.
     * Được tự động điền bởi Spring Data JPA Auditing.
     */
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    /**
     * Tên đăng nhập của người dùng đã sửa đổi thực thể lần cuối.
     * Được tự động điền bởi Spring Data JPA Auditing.
     */
    @LastModifiedBy
    @Column(name = "updated_by", length = 50)
    private String updatedBy;

    /**
     * Thời điểm (timestamp) thực thể được sửa đổi lần cuối.
     * Được tự động điền bởi Spring Data JPA Auditing.
     */
    @LastModifiedDate
    @Column(name = "updated_at")
    private Instant updatedAt = Instant.now();

    /**
     * Cờ đánh dấu thực thể đã bị xóa mềm hay chưa.
     * Mặc định là {@code false}.
     */
    @ColumnDefault("false")
    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;

    /**
     * Thời điểm (timestamp) thực thể bị xóa mềm.
     */
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    /**
     * Tên đăng nhập của người dùng đã thực hiện xóa mềm.
     */
    @Column(name = "deleted_by", length = 50)
    private String deletedBy;

    @Override
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * Phương thức này chủ yếu dành cho các bài test hoặc các trường hợp đặc biệt.
     * Trong hoạt động bình thường, trường này được quản lý bởi JPA Auditing.
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public Instant getCreatedAt() {
        return createdAt;
    }

    /**
     * Phương thức này chủ yếu dành cho các bài test hoặc các trường hợp đặc biệt.
     * Trong hoạt động bình thường, trường này được quản lý bởi JPA Auditing.
     */
    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String getUpdatedBy() {
        return updatedBy;
    }

    /**
     * Phương thức này chủ yếu dành cho các bài test hoặc các trường hợp đặc biệt.
     * Trong hoạt động bình thường, trường này được quản lý bởi JPA Auditing.
     */
    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    @Override
    public Instant getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Phương thức này chủ yếu dành cho các bài test hoặc các trường hợp đặc biệt.
     * Trong hoạt động bình thường, trường này được quản lý bởi JPA Auditing.
     */
    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * Trả về {@code true} nếu thực thể đã bị xóa mềm, ngược lại là {@code false}.
     */
    public boolean getDeleted() {
        return deleted;
    }

    /**
     * Đặt trạng thái xóa mềm cho thực thể.
     */
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    /**
     * Lấy thời điểm thực thể bị xóa mềm.
     */
    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    /**
     * Đặt thời điểm thực thể bị xóa mềm.
     */
    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    /**
     * Lấy tên người dùng đã xóa mềm thực thể.
     */
    public String getDeletedBy() {
        return deletedBy;
    }

    /**
     * Đặt tên người dùng đã xóa mềm thực thể.
     */
    public void setDeletedBy(String deletedBy) {
        this.deletedBy = deletedBy;
    }
    
}