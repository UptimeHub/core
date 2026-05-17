package uz.uptimehub.coreapp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import uz.uptimehub.core.dto.organizationadminassignment.AdminCreateRequest;
import uz.uptimehub.core.dto.organizationadminassignment.UserRole;
import uz.uptimehub.core.dto.user.UserResponse;
import uz.uptimehub.coreapp.service.AdminUserService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/core/admin-user/")
@RequiredArgsConstructor
public class AdminUserController {
    private final AdminUserService adminUserService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createAdmin(@RequestBody AdminCreateRequest requestBody) {
        adminUserService.createAdmin(requestBody);
    }

    @GetMapping("/all")
    public List<UserResponse> getAllAdmins(@RequestParam UserRole role, @RequestParam(required = false) UUID organizationId) {
        return adminUserService.getAllAdmins(role, organizationId);
    }
}
