package uz.uptimehub.coreapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.uptimehub.core.exception.EntityNotFoundException;
import uz.uptimehub.coreapp.jpa.entity.Organization;
import uz.uptimehub.coreapp.jpa.entity.OrganizationAdminAssignment;
import uz.uptimehub.coreapp.jpa.repository.OrganizationAdminAssignmentRepository;
import uz.uptimehub.coreapp.jpa.repository.OrganizationRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrganizationAdminService {
    private final KeycloakService keycloakService;
    private final OrganizationAdminAssignmentRepository adminAssignmentRepository;
    private final OrganizationRepository organizationRepository;

    @Transactional
    public void createOrganizationAdmin(UUID organizationId, String username, String email, String firstName, String lastName, String password) {
        Organization organization = findOrganizationAndValidateId(organizationId);

        String userId = keycloakService.createOrganizationAdmin(organizationId, username, email, firstName, lastName, password);

        OrganizationAdminAssignment adminUserRecord = OrganizationAdminAssignment.builder()
                .organization(organization)
                .keycloakUserId(UUID.fromString(userId))
                .build();

        adminAssignmentRepository.save(adminUserRecord);

    }

    private Organization findOrganizationAndValidateId (UUID id) {
        return organizationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Organization not found"));
    }
}
