package uz.uptimehub.coreapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import uz.uptimehub.core.dto.providertype.ProviderTypeCreateRequest;
import uz.uptimehub.core.dto.providertype.ProviderTypeDto;
import uz.uptimehub.core.dto.providertype.ProviderTypeFilter;
import uz.uptimehub.core.exception.InvalidSortRule;
import uz.uptimehub.core.pagination.FilteredSortedPaginatedRequest;
import uz.uptimehub.coreapp.service.ProviderTypeService;


@RestController
@RequestMapping("/api/provider-type")
@RequiredArgsConstructor
@Validated
@Tag(name = "Provider Type Controller", description = "Provider Type management API")
public class ProviderTypeController {
    private final ProviderTypeService providerTypeService;

    @PostMapping
    @Operation(description = "Create a new provider type")
    public ProviderTypeDto createProviderType(@RequestBody ProviderTypeCreateRequest request) {
        return providerTypeService.save(request);
    }

    @PatchMapping
    @Operation(description = "Update an existing provider type")
    public ProviderTypeDto updateProviderType(@RequestBody ProviderTypeDto request) {
        return providerTypeService.update(request);
    }


    @GetMapping("/all")
    @Operation(description = "Get all provider types")
    public Page<ProviderTypeDto> getProviderTypesByFilter(
            @Parameter(description = "Filter criteria")
            ProviderTypeFilter filter,
            Pageable pageable
    ) {
        return providerTypeService.getAll(new FilteredSortedPaginatedRequest<>(filter, pageable, InvalidSortRule::new));
    }

    @GetMapping("/{id}")
    @Operation(description = "Get provider type by ID")
    public ProviderTypeDto getProviderTypeById(@PathVariable Long id) {
        return providerTypeService.getById(id);
    }


}
