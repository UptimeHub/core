package uz.uptimehub.coreapi.dto.organizationadminassignment;

import uz.uptimehub.coreapi.dto.organization.OrganizationSummaryResponse;

import java.time.LocalDateTime;
import java.util.UUID;

public record OrganizationAdminAssignmentResponse(
        OrganizationSummaryResponse organizationResponse,
        UUID keycloakUserId,
        LocalDateTime createdAt
) {
}
