package uz.uptimehub.core.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import uz.uptimehub.core.dto.organization.OrganizationDetailedResponse;
import uz.uptimehub.core.dto.organization.OrganizationFilter;
import uz.uptimehub.core.dto.organization.OrganizationSummaryResponse;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "organization-service", path = "/api/organization")
public interface OrganizationClient {

    @GetMapping("/all")
    Page<OrganizationSummaryResponse> getOrganizations(
            @SpringQueryMap OrganizationFilter filter,
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam(required = false) List<String> sort
    );

    @GetMapping("/detailed/{id}")
    OrganizationDetailedResponse getOrganizationDetailedById(@PathVariable UUID id);

    @GetMapping("/summary/{id}")
    OrganizationSummaryResponse getOrganizationSummaryById(@PathVariable UUID id);
}
