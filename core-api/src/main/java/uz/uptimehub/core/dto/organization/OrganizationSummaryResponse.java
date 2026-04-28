package uz.uptimehub.core.dto.organization;

import java.util.UUID;

public record OrganizationSummaryResponse (
        UUID id,
        String name,
        String taxpayerIdNumber,
        String email,
        Long providerTypeId
){
}
