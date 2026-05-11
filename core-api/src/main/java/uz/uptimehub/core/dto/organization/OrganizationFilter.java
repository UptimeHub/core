package uz.uptimehub.core.dto.organization;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import uz.uptimehub.core.pagination.Filter;
import uz.uptimehub.core.pagination.IdPropertyOverride;
import uz.uptimehub.core.pagination.SortPropertyOverride;

@Schema(description = "Organization filter")
@IdPropertyOverride("o.id")
@Getter
@Setter
public class OrganizationFilter extends Filter {
    @Schema(description = "Organization name")
    @SortPropertyOverride( "o.name")
    private String name;
    @Schema(description = "Taxpayer ID number")
    @SortPropertyOverride("o.taxpayerIdNumber")
    private String taxpayerIdNumber;
    @Schema(description = "Email")
    @SortPropertyOverride("o.email")
    private String email;
    @Schema(description = "Organization status")
    @SortPropertyOverride("o.status")
    private Status status;
    @Schema(description = "Provider type ID")
    @SortPropertyOverride("o.providerTypeId")
    private Long providerTypeId;
}
