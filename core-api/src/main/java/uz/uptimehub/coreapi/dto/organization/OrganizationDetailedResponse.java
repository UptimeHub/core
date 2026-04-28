package uz.uptimehub.coreapi.dto.organization;

import java.time.LocalDateTime;
import java.util.UUID;

public record OrganizationDetailedResponse (
        UUID id,
        String name,
        String taxpayerIdNumber,
        String email,
        Status status,
        Long providerTypeId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
