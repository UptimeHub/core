package uz.uptimehub.coreapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Admin User Controller", description = "API for managing admin users and their roles within organizations")
public class AdminUserController {
    private final AdminUserService adminUserService;

    @PostMapping
    @Operation(description = "Create a new admin user and assign them a role within an organization. Only Super Admins can create admin users.")
    @ResponseStatus(HttpStatus.CREATED)
    public void createAdmin(@RequestBody AdminCreateRequest requestBody) {
        adminUserService.createAdmin(requestBody);
    }

    @GetMapping("/all")
    @Operation(description = "Get all admin users with a specific role. Optionally filter by organization. Only Super Admins can access this endpoint.")
    public List<UserResponse> getAllAdmins(@RequestParam UserRole role, @RequestParam(required = false) UUID organizationId) {
        return adminUserService.getAllAdmins(role, organizationId);
    }
}
