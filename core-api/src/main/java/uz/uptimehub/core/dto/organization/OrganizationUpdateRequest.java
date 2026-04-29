package uz.uptimehub.core.dto.organization;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrganizationUpdateRequest {
        @NotNull(message = "Organization ID is required")
        private UUID id;
        private String name;
        private String taxpayerIdNumber;
        private String email;
}
