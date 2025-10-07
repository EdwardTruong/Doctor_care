package com.example.doctorcare.domain.business.clinics_;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.doctorcare.core.domain.BaseRepository;

@Repository
public interface ClinicsRepository extends BaseRepository<Clinics, Long>, ClinicsRepositoryCustom {

	/**
	 * Tìm clinic theo tên và chưa bị xóa
	 */
	Optional<Clinics> findByNameAndDeletedFalse(String name);

	/**
	 * Tìm clinic theo owner ID
	 */
	Optional<Clinics> findByOwnerIdAndDeletedFalse(Long ownerId);

	/**
	 * Tìm clinics theo place ID
	 */
	Page<Clinics> findByPlaceIdAndDeletedFalse(Long placeId, Pageable pageable);

	/**
	 * Tìm clinics theo keyword trong tên hoặc địa chỉ
	 */
	@Query("SELECT c FROM Clinics c WHERE c.deleted = false " +
		   "AND (:keyword IS NULL OR " +
		   "     LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
		   "     LOWER(c.address) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
		   "     LOWER(c.description) LIKE LOWER(CONCAT('%', :keyword, '%')))")
	Page<Clinics> findByKeyword(@Param("keyword") String keyword, Pageable pageable);

	/**
	 * Lấy top clinics phổ biến nhất (sửa lại query)
	 */
	@Query("SELECT c FROM Clinics c " +
		   "LEFT JOIN c.listDoctor d " +
		   "WHERE c.deleted = false " +
		   "GROUP BY c.id " +
		   "ORDER BY COUNT(d.id) DESC, c.view DESC")
	List<Clinics> findTopClinics(Pageable pageable);

	/**
	 * Đếm số doctors trong clinic
	 */
	@Query("SELECT COUNT(d) FROM Doctor d WHERE d.clinic.id = :clinicId")
	Long countDoctorsByClinicId(@Param("clinicId") Long clinicId);

	/**
	 * Tìm clinics có doctors
	 */
	@Query("SELECT DISTINCT c FROM Clinics c " +
		   "INNER JOIN c.listDoctor d " +
		   "WHERE c.deleted = false")
	Page<Clinics> findClinicsWithDoctors(Pageable pageable);

	/**
	 * Kiểm tra clinic có tồn tại theo tên (trừ clinic hiện tại)
	 */
	@Query("SELECT COUNT(c) > 0 FROM Clinics c WHERE " +
		   "LOWER(c.name) = LOWER(:name) AND c.deleted = false " +
		   "AND (:excludeId IS NULL OR c.id != :excludeId)")
	boolean existsByNameExcludingId(@Param("name") String name, @Param("excludeId") Long excludeId);
}
