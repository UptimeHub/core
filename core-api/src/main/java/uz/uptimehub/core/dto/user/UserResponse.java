package uz.uptimehub.core.dto.user;

import java.util.UUID;

public record UserResponse(
        UUID id,
        String firstName,
        String lastName,
        String username,
        String email,
        String role,
        UUID organizationId
) {
}
