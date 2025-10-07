package com.example.doctorcare.application.service.manager.query;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.doctorcare.application.service.manager.dto.ManagerDto;
import com.example.doctorcare.core.cqrs.annotation.CqrsQueryHandler;
import com.example.doctorcare.core.cqrs.handler.QueryHandler;
import com.example.doctorcare.domain.business.manager.Manager;
import com.example.doctorcare.domain.business.manager.ManagerRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@CqrsQueryHandler(GetManagerHierarchyQuery.class)
public class GetManagerHierarchyQueryHandler implements QueryHandler<GetManagerHierarchyQuery, List<ManagerDto>> {

    private final ManagerRepository managerRepository;

    @Override
    public List<ManagerDto> handle(GetManagerHierarchyQuery query) {
        log.debug("Getting manager hierarchy - rootId: {}, maxLevel: {}", 
                 query.rootId(), query.maxLevel());

        List<Manager> allManagers;
        
        if (query.rootId() != null) {
            // Lấy cây con từ root manager
            allManagers = managerRepository.findManagersHierarchy(query.rootId());
        } else {
            // Lấy toàn bộ cây (từ các root managers)
            allManagers = managerRepository.findAllActiveManagers();
        }

        // Xây dựng cây phân cấp
        List<ManagerDto> hierarchyTree = buildHierarchyTree(allManagers, query.rootId(), query.maxLevel());

        log.debug("Built manager hierarchy with {} root nodes", hierarchyTree.size());

        return hierarchyTree;
    }

    private List<ManagerDto> buildHierarchyTree(List<Manager> allManagers, Long rootId, Integer maxLevel) {
        // Nhóm managers theo parentId
        Map<Long, List<Manager>> managersByParent = allManagers.stream()
            .filter(m -> m.getParentManager() != null)
            .collect(Collectors.groupingBy(m -> m.getParentManager().getId()));

        List<Manager> rootManagers;
        
        if (rootId != null) {
            // Tìm manager root cụ thể
            rootManagers = allManagers.stream()
                .filter(m -> m.getId().equals(rootId))
                .collect(Collectors.toList());
        } else {
            // Lấy tất cả root managers (không có parent)
            rootManagers = allManagers.stream()
                .filter(m -> m.getParentManager() == null)
                .collect(Collectors.toList());
        }

        List<ManagerDto> result = new ArrayList<>();
        
        for (Manager rootManager : rootManagers) {
            ManagerDto rootDto = ManagerDto.fromEntity(rootManager);
            buildChildrenRecursively(rootDto, managersByParent, 1, maxLevel);
            result.add(rootDto);
        }

        return result;
    }

    private void buildChildrenRecursively(ManagerDto parentDto, 
                                         Map<Long, List<Manager>> managersByParent, 
                                         int currentLevel, 
                                         Integer maxLevel) {
        
        // Kiểm tra giới hạn cấp độ
        if (maxLevel != null && currentLevel >= maxLevel) {
            return;
        }

        List<Manager> children = managersByParent.get(parentDto.getId());
        if (children == null || children.isEmpty()) {
            return;
        }

        List<ManagerDto> childrenDtos = new ArrayList<>();
        
        for (Manager child : children) {
            ManagerDto childDto = ManagerDto.fromEntity(child);
            
            // Đệ quy xây dựng cây con
            buildChildrenRecursively(childDto, managersByParent, currentLevel + 1, maxLevel);
            
            childrenDtos.add(childDto);
        }

        // Set children cho parent (cần thêm setter trong ManagerDto)
        parentDto.setChildren(childrenDtos);
    }
}