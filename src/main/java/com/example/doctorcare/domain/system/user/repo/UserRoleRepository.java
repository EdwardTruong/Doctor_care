package com.example.doctorcare.domain.system.user.repo;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.example.doctorcare.core.domain.BaseRepository;
import com.example.doctorcare.domain.system.user.UserRole;


public interface UserRoleRepository extends BaseRepository<UserRole, Long> {

    /**
     * Tìm kiếm UserRole theo userId và roleId.
     *
     * @param userId ID của người dùng.
     * @param roleId ID của vai trò.
     * @return UserRole tương ứng với userId và roleId đã cho.
     */
    UserRole findByUserIdAndRoleId(Long userId, Long roleId);

    /**
     * Tìm kiếm tất cả các UserRole theo userId.
     *
     * @param userId ID của người dùng.
     * @return List các UserRole tương ứng với userId đã cho.
     */
    List<UserRole> findByUserId(Long userId);

    /**
     * Tìm kiếm tất cả các UserRole theo userId và fetch đồng thời thông tin Role để tránh N+1 query.
     *
     * @param userId ID của người dùng.
     * @return List các UserRole tương ứng với userId đã cho, với thông tin Role đã được tải.
     */
    @Query("SELECT ur FROM UserRole ur JOIN FETCH ur.role WHERE ur.user.id = :userId")
    List<UserRole> findByUserIdWithRole(@Param("userId") Long userId);

    /**
     * Xóa UserRole theo userId và roleId.
     *
     * @param userId ID của người dùng.
     * @param roleId ID của vai trò.
     */
    void deleteByUserIdAndRoleId(Long userId, Long roleId);

    boolean existsByRoleId(Long id);

}
