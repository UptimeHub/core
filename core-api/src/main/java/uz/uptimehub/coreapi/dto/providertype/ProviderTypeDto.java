package uz.uptimehub.coreapi.dto.providertype;

import jakarta.validation.constraints.NotNull;

public record ProviderTypeDto(
        @NotNull(message = "Provider type ID is required")
        Long id,
        String name,
        String description
) {
}
