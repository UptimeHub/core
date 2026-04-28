package uz.uptimehub.coreapi.dto.organization;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record OrganizationCreateRequest(
        @NotBlank(message = "Organization name is required")
        String name,
        @NotBlank(message = "Taxpayer ID number is required")
        String taxpayerIdNumber,
        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        String email,
        @NotNull(message = "Provider type ID is required")
        Long providerTypeId
) {
}
