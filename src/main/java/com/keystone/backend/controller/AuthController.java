package com.keystone.backend.controller;
import java.util.Map;
import com.keystone.backend.domain.AppUser;
import com.keystone.backend.dto.LoginRequest;
import com.keystone.backend.dto.LoginResponse;
import com.keystone.backend.repository.AppUserRepository;
import com.keystone.backend.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final AppUserRepository appUserRepository;

    @PostMapping("/login")
public ResponseEntity<?> login(@RequestBody LoginRequest request) {
    try {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        String token = jwtService.generateToken(userDetails);
        AppUser appUser = appUserRepository.findByEmail(request.getEmail()).orElseThrow();

        return ResponseEntity.ok(new LoginResponse(token, appUser.getEmail(), appUser.getRole().name()));
    } catch (org.springframework.security.core.AuthenticationException ex) {
        return ResponseEntity.status(401).body(Map.of("message", "Invalid email or password"));
    }
}

}