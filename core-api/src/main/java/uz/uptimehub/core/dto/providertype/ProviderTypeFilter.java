package uz.uptimehub.core.dto.providertype;

import io.swagger.v3.oas.annotations.media.Schema;
import uz.uptimehub.core.pagination.Filter;
import uz.uptimehub.core.pagination.IdPropertyOverride;
import uz.uptimehub.core.pagination.SortPropertyOverride;

@Schema(description = "Provider type filter")
@IdPropertyOverride("pt.id")
public class ProviderTypeFilter extends Filter {
    @Schema(description = "Provider type name")
    @SortPropertyOverride("pt.name")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
