package uz.uptimehub.coreapp.controller;

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
public class ProviderTypeController {
    private final ProviderTypeService providerTypeService;

    @PostMapping
    public ProviderTypeDto createProviderType(@RequestBody ProviderTypeCreateRequest request) {
        return providerTypeService.save(request);
    }

    @PatchMapping
    public ProviderTypeDto updateProviderType(@RequestBody ProviderTypeDto request) {
        return providerTypeService.update(request);
    }


    @GetMapping("/all")
    public Page<ProviderTypeDto> getProviderTypesByFilter(
            ProviderTypeFilter filter,
            Pageable pageable
    ) {
        return providerTypeService.getAll(new FilteredSortedPaginatedRequest<>(filter, pageable, InvalidSortRule::new));
    }

    @GetMapping("/{id}")
    public ProviderTypeDto getProviderTypeById(@PathVariable Long id) {
        return providerTypeService.getById(id);
    }


}
