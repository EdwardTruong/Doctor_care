package com.example.doctorcare.application.service.user.query;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.doctorcare.application.service.user.dto.UserDetailDto;
import com.example.doctorcare.core.cqrs.annotation.CqrsPageQueryHandler;
import com.example.doctorcare.core.cqrs.handler.PageQueryHandler;
import com.example.doctorcare.core.cqrs.utils.PageImpl;
import com.example.doctorcare.domain.system.user.User;
import com.example.doctorcare.domain.system.user.repo.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@CqrsPageQueryHandler(GetUsersQuery.class)
public class GetUsersQueryHandler implements PageQueryHandler<GetUsersQuery, UserDetailDto> {

    private final UserRepository userRepository;

    @Override
    public com.example.doctorcare.core.cqrs.utils.Page<UserDetailDto> handle(GetUsersQuery query) {
        log.debug("Getting users with keyword: {}, active: {}, gender: {}", 
                query.keyword(), query.active(), query.gender());

        // Create sort
        Sort sort = Sort.Direction.DESC.name().equalsIgnoreCase(query.sortDirection())
                ? Sort.by(query.sortBy()).descending()
                : Sort.by(query.sortBy()).ascending();

        PageRequest pageRequest = PageRequest.of(
                query.pageable().getPageNumber(),
                query.pageable().getPageSize(),
                sort
        );

        // Get all users and filter manually (for simplicity)
        // In production, you should use database-level filtering with Specifications
        List<User> allUsers = userRepository.findAll();
        List<User> filteredUsers = allUsers.stream()
                .filter(user -> query.active() == null || user.isActive() == query.active())
                .filter(user -> query.gender() == null || user.getGender() == query.gender())
                .filter(user -> {
                    if (query.keyword() == null || query.keyword().trim().isEmpty()) {
                        return true;
                    }
                    String keyword = query.keyword().toLowerCase();
                    String fullName = user.getFullName() != null ? user.getFullName().toLowerCase() : "";
                    String email = user.getAddressEmail() != null ? user.getAddressEmail().toLowerCase() : "";
                    String username = user.getUsername() != null ? user.getUsername().toLowerCase() : "";
                    
                    return fullName.contains(keyword) || email.contains(keyword) || username.contains(keyword);
                })
                .collect(Collectors.toList());

        // Apply sorting
        if (sort.isSorted()) {
            filteredUsers = filteredUsers.stream()
                    .sorted((u1, u2) -> {
                        String sortProperty = query.sortBy();
                        boolean isDesc = Sort.Direction.DESC.name().equalsIgnoreCase(query.sortDirection());
                        
                        int comparison = 0;
                        switch (sortProperty) {
                            case "fullName":
                                comparison = compareStrings(u1.getFullName(), u2.getFullName());
                                break;
                            case "addressEmail":
                                comparison = compareStrings(u1.getAddressEmail(), u2.getAddressEmail());
                                break;
                            case "username":
                                comparison = compareStrings(u1.getUsername(), u2.getUsername());
                                break;
                            case "active":
                                comparison = Boolean.compare(u1.isActive(), u2.isActive());
                                break;
                            default:
                                comparison = u1.getId().compareTo(u2.getId());
                        }
                        return isDesc ? -comparison : comparison;
                    })
                    .collect(Collectors.toList());
        }

        // Apply pagination
        int start = Math.min((int) pageRequest.getOffset(), filteredUsers.size());
        int end = Math.min(start + pageRequest.getPageSize(), filteredUsers.size());
        List<User> pageContent = filteredUsers.subList(start, end);

        // Convert to DTOs
        List<UserDetailDto> userDtos = pageContent.stream()
                .map(UserDetailDto::fromEntity)
                .collect(Collectors.toList());

        return new PageImpl<>(
                userDtos,
                query.pageable().getPageNumber(),
                query.pageable().getPageSize(),
                filteredUsers.size()
        );
    }

    private int compareStrings(String s1, String s2) {
        if (s1 == null && s2 == null) return 0;
        if (s1 == null) return -1;
        if (s2 == null) return 1;
        return s1.compareTo(s2);
    }
}