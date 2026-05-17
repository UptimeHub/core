package uz.uptimehub.coreapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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
@RequestMapping("/api/core/organization")
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
    public OrganizationDetailedResponse updateOrganization(
            @Valid @RequestBody OrganizationUpdateRequest requestBody,
            HttpServletRequest request
    ) {
        UUID orgId = organizationService.organizationIdOverride(request);
        if (orgId != null)
            requestBody.setId(orgId);

        return organizationService.update(requestBody);
    }

    @GetMapping("/all")
    @Operation(description = "Get all organizations. Only Admins can get access")
    public Page<OrganizationSummaryResponse> getOrganizationsByFilter(
            @Parameter(description = "Filter criteria")
            @ParameterObject OrganizationFilter filter,
            @ParameterObject Pageable pageable
    ) {
        return organizationService.findAll(
                new FilteredSortedPaginatedRequest<>(filter, pageable, InvalidSortRule::new)
        );
    }

    @GetMapping("/detailed/{id}")
    @Operation(description = "Get detailed information about an organization. Only Organization Manager and Admins can get access")
    public OrganizationDetailedResponse getOrganizationDetailedById(@PathVariable UUID id, HttpServletRequest request) {
        UUID overwrittenId = organizationService.organizationIdOverride(request);

        return organizationService.getDetailedById(overwrittenId != null ? overwrittenId : id);
    }

    @GetMapping("/summary/{id}")
    @Operation(description = "Get summary information about an organization. Open to every role")
    public OrganizationSummaryResponse getOrganizationSummaryById(@PathVariable UUID id) {
        return organizationService.getSummaryById(id);
    }

    @GetMapping("/filters")
    @Operation(description = "Get available filters for organizations. Only admins can get access")
    public Map<String, Set<String>> getFiltersMap() {
        return organizationService.getFiltersMap();
    }

    @PostMapping("/status")
    @Operation(description = "Activate/Deactivate organization. Only platform admins can access")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void setOrganizationStatus(@RequestParam UUID organizationId, @RequestParam boolean activate) {
        organizationService.setOrganizationStatus(organizationId, activate);
    }
}
