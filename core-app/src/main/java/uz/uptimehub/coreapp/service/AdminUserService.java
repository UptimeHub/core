package uz.uptimehub.coreapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.uptimehub.core.dto.organizationadminassignment.AdminCreateRequest;
import uz.uptimehub.core.dto.organizationadminassignment.UserRole;
import uz.uptimehub.core.dto.user.UserResponse;
import uz.uptimehub.core.exception.EntityNotFoundException;
import uz.uptimehub.coreapp.jpa.entity.Organization;
import uz.uptimehub.coreapp.jpa.entity.OrganizationAdminAssignment;
import uz.uptimehub.coreapp.jpa.repository.OrganizationAdminAssignmentRepository;
import uz.uptimehub.coreapp.jpa.repository.OrganizationRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminUserService {
    private final KeycloakService keycloakService;
    private final OrganizationAdminAssignmentRepository adminAssignmentRepository;
    private final OrganizationRepository organizationRepository;

    @Transactional
    public void createAdmin(AdminCreateRequest request) {
        switch (request.role()) {
            case PLATFORM_ADMIN -> createPlatformAdmin(request);
            case ORGANIZATION_ADMIN -> createOrganizationAdmin(request);
        }
    }


    public void createPlatformAdmin(AdminCreateRequest requestBody) {
        keycloakService.createAdmin(
                requestBody.organizationId(),
                requestBody.username(),
                requestBody.email(),
                requestBody.firstName(),
                requestBody.lastName(),
                requestBody.password(),
                requestBody.role()
        );
    }

    public void createOrganizationAdmin(AdminCreateRequest requestBody) {
        Organization organization = findOrganizationAndValidateId(requestBody.organizationId());

        String userId = keycloakService.createAdmin(
                requestBody.organizationId(),
                requestBody.username(),
                requestBody.email(),
                requestBody.firstName(),
                requestBody.lastName(),
                requestBody.password(),
                requestBody.role()
        );

        OrganizationAdminAssignment adminUserRecord = OrganizationAdminAssignment.builder()
                .organization(organization)
                .keycloakUserId(UUID.fromString(userId))
                .build();

        adminAssignmentRepository.save(adminUserRecord);
    }
    public List<UserResponse> getAllAdmins(UserRole role, UUID organizationId) {
        return keycloakService.getAllAdminsByRoleType(role, organizationId);
    }

    private Organization findOrganizationAndValidateId (UUID id) {
        return organizationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Organization not found"));
    }
}
