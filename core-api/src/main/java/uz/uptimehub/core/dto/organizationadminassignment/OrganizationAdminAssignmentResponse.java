package uz.uptimehub.core.dto.organizationadminassignment;

import uz.uptimehub.core.dto.organization.OrganizationSummaryResponse;

import java.time.LocalDateTime;
import java.util.UUID;

public record OrganizationAdminAssignmentResponse(
        OrganizationSummaryResponse organizationResponse,
        UUID keycloakUserId,
        LocalDateTime createdAt
) {
}
