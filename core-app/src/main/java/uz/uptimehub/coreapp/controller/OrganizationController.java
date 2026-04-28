package uz.uptimehub.coreapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import uz.uptimehub.core.dto.organization.OrganizationCreateRequest;
import uz.uptimehub.core.dto.organization.OrganizationDetailedResponse;
import uz.uptimehub.core.dto.organization.OrganizationFilter;
import uz.uptimehub.core.dto.organization.OrganizationSummaryResponse;
import uz.uptimehub.core.dto.organization.OrganizationUpdateRequest;
import uz.uptimehub.core.exception.InvalidSortRule;
import uz.uptimehub.core.pagination.FilteredSortedPaginatedRequest;
import uz.uptimehub.coreapp.service.OrganizationService;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/organization")
@CrossOrigin
@Tag(name = "Organization Controller", description = "Organization management API")
public class OrganizationController {
    private final OrganizationService organizationService;

    @PostMapping
    @Operation(description = "Create a new organization")
    public OrganizationDetailedResponse createOrganization(@Valid @RequestBody OrganizationCreateRequest request) {
        return organizationService.save(request);
    }

    @PatchMapping
    @Operation(description = "Update an existing organization")
    public OrganizationDetailedResponse updateOrganization(@Valid @RequestBody OrganizationUpdateRequest request) {
        return organizationService.update(request);
    }

    @GetMapping("/all")
    @Operation(description = "Get all organizations")
    public Page<OrganizationSummaryResponse> getOrganizationsByFilter(
            @Parameter(description = "Filter criteria")
            OrganizationFilter filter,
            Pageable pageable
    ) {
        return organizationService.findAll(
                new FilteredSortedPaginatedRequest<>(filter, pageable, InvalidSortRule::new)
        );
    }

    @GetMapping("/detailed/{id}")
    @Operation(description = "Get detailed information about an organization")
    public OrganizationDetailedResponse getOrganizationDetailedById(@PathVariable UUID id) {
        return organizationService.getDetailedById(id);
    }

    @GetMapping("/summary/{id}")
    @Operation(description = "Get summary information about an organization")
    public OrganizationSummaryResponse getOrganizationSummaryById(@PathVariable UUID id) {
        return organizationService.getSummaryById(id);
    }

    @GetMapping("/filters")
    @Operation(description = "Get available filters for organizations")
    public Map<String, Set<String>> getFiltersMap() {
        return organizationService.getFiltersMap();
    }
}
