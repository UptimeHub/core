package uz.uptimehub.core.dto.organization;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record OrganizationUpdateRequest(
        @NotNull(message = "Organization ID is required")
        UUID id,
        String name,
        String taxpayerIdNumber,
        String email
) {
}
