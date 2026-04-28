package uz.uptimehub.coreapi.dto.providertype;

import jakarta.validation.constraints.NotBlank;

public record ProviderTypeCreateRequest(
        @NotBlank(message = "Provider type name is required")
        String name,
        @NotBlank(message = "Provider type description is required")
        String description
) {
}
