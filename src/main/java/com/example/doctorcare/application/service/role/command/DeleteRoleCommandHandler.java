package com.example.doctorcare.application.service.role.command;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.doctorcare.application.exception.RoleInUseException;
import com.example.doctorcare.application.exception.RoleNotFoundException;
import com.example.doctorcare.core.cqrs.annotation.CqrsCommandHandler;
import com.example.doctorcare.core.cqrs.handler.CommandHandler;
import com.example.doctorcare.domain.business.doctor.DoctorRepository;
import com.example.doctorcare.domain.business.manager.ManagerRepository;
import com.example.doctorcare.domain.system.role.model.Role;
import com.example.doctorcare.domain.system.role.model.RolePermission;
import com.example.doctorcare.domain.system.role.repository.RolePermissionRepository;
import com.example.doctorcare.domain.system.role.repository.RoleRepository;
import com.example.doctorcare.domain.system.user.repo.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
@RequiredArgsConstructor
@CqrsCommandHandler(DeleteRoleCommand.class)
public class DeleteRoleCommandHandler implements CommandHandler<DeleteRoleCommand> {

    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final DoctorRepository doctorRepository;
    private final ManagerRepository managerRepository;
    private final RolePermissionRepository rolePermissionRepository;

    @Override
    @Transactional
    public void handle(DeleteRoleCommand command) {
        Role roleToDelete = roleRepository.findById(command.id())
                .orElseThrow(() -> new RoleNotFoundException(command.id()));

        // Kiểm tra xem vai trò có đang được gán cho người dùng toàn cục không.
        if (userRoleRepository.existsByRoleId(command.id())) {
            throw new RoleInUseException(roleToDelete.getRoleName(), "Vai trò đang được gán cho người dùng.");
        }

        // Kiểm tra xem vai trò có đang được gán cho bác sỹ hay không.
        if (doctorRepository.existsByRoleId(command.id())) {
            throw new RoleInUseException(roleToDelete.getRoleName(), "Vai trò đang được gán cho nhân viên trong một doanh nghiệp.");
        }

        // Kiểm tra xem có đang quản lý .
        if (managerRepository.existsByRoleId(command.id())) {
            throw new RoleInUseException(roleToDelete.getRoleName(), "Vai trò đang được gán cho thành viên trong một tổ chức/hội.");
        }

        // Kiểm tra xem vai trò có đang là cha của vai trò khác không.
        if (roleRepository.existsByParentRoleId(command.id())) {
            throw new RoleInUseException(roleToDelete.getRoleName(), "Vai trò này đang là cha của các vai trò khác. Cần gỡ bỏ mối quan hệ cha-con trước khi xóa.");
        }

        // Xóa các bản ghi trong role_permission trước khi xóa vai trò để tránh lỗi ràng buộc khóa ngoại.
        List<RolePermission> associatedPermissions = rolePermissionRepository.findByRoleId(command.id());
        if (!associatedPermissions.isEmpty()) {
            rolePermissionRepository.deleteAllInBatch(associatedPermissions);
            log.debug("Đã xóa {} liên kết quyền của vai trò '{}'", associatedPermissions.size(), roleToDelete.getRoleName());
        }

        roleRepository.delete(roleToDelete);

    }
}