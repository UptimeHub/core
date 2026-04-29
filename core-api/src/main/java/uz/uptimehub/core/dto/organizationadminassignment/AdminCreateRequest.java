package uz.uptimehub.core.dto.organizationadminassignment;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Schema(description = "Admin create request")
public record AdminCreateRequest(
        @Schema(description = "Organization ID, keep null if role is PLATFORM_ADMIN")
        UUID organizationId,
        @Schema(description = "First name of the admin")
        @NotBlank(message = "First name is required")
        String firstName,
        @Schema(description = "Last name of the admin")
        @NotBlank(message = "Last name is required")
        String lastName,
        @Schema(description = "Username of the admin")
        @NotBlank(message = "Username is required")
        String username,
        @Schema(description = "Email of the admin")
        @NotBlank(message = "Email is required")
        String email,
        @Schema(description = "Password of the admin")
        @NotBlank(message = "Password is required")
        String password,
        @Schema(description = "Role of the admin, either ORGANIZATION_ADMIN or PLATFORM_ADMIN")
        @NotNull(message = "Role is required")
        UserRole role
) {
}
