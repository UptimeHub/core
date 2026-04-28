package uz.uptimehub.core.dto.providertype;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import uz.uptimehub.core.pagination.Filter;
import uz.uptimehub.core.pagination.IdPropertyOverride;
import uz.uptimehub.core.pagination.SortPropertyOverride;

@Setter
@Getter
@Schema(description = "Provider type filter")
@IdPropertyOverride("pt.id")
public class ProviderTypeFilter extends Filter {
    @Schema(description = "Provider type name")
    @SortPropertyOverride("pt.name")
    private String name;
}
