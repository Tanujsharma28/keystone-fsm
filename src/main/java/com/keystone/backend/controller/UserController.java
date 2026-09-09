package com.keystone.backend.controller;

import com.keystone.backend.domain.Role;
import com.keystone.backend.dto.UserResponse;
import com.keystone.backend.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final AppUserRepository appUserRepository;

    // Dispatcher ko work order assign karte waqt technicians ki list chahiye hoti hai
    @GetMapping("/technicians")
    @PreAuthorize("hasAnyRole('MANAGER','DISPATCHER')")
    public ResponseEntity<List<UserResponse>> getTechnicians() {
        List<UserResponse> technicians = appUserRepository.findByRole(Role.TECHNICIAN)
                .stream()
                .map(u -> new UserResponse(u.getId(), u.getFullName(), u.getEmail(), null))
                .toList();
        return ResponseEntity.ok(technicians);
    }

    // Har logged-in user apni khud ki details maang sakta hai
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser(Authentication authentication) {
        return appUserRepository.findByEmail(authentication.getName())
                .map(u -> ResponseEntity.ok(new UserResponse(
                        u.getId(),
                        u.getFullName(),
                        u.getEmail(),
                        u.getCustomer() != null ? u.getCustomer().getId() : null
                )))
                .orElseThrow();
    }
}