package uz.uptimehub.coreapp.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.uptimehub.core.dto.organization.*;
import uz.uptimehub.core.exception.EntityNotFoundException;
import uz.uptimehub.core.exception.InvalidSortRule;
import uz.uptimehub.core.pagination.FilteredSortedPaginatedRequest;
import uz.uptimehub.coreapp.jpa.entity.Organization;
import uz.uptimehub.coreapp.jpa.entity.ProviderType;
import uz.uptimehub.coreapp.jpa.repository.OrganizationRepository;
import uz.uptimehub.coreapp.jpa.repository.ProviderTypeRepository;
import uz.uptimehub.coreapp.mapper.OrganizationMapper;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrganizationService {

    private final OrganizationRepository organizationRepository;
    private final ProviderTypeRepository providerTypeRepository;
    private final OrganizationMapper organizationMapper;

    public OrganizationDetailedResponse save(OrganizationCreateRequest request) {
        ProviderType providerType = findProviderTypeAndValidateId(request.providerTypeId());
        return organizationMapper.toDetailedResponse(organizationRepository.save(organizationMapper.toOrganization(request, providerType)));
    }

    @Transactional
    public OrganizationDetailedResponse update(OrganizationUpdateRequest request) {
        Organization organization = findOrganizationAndValidateId(request.getId());

        organizationMapper.updateOrganization(request, organization);

        return organizationMapper.toDetailedResponse(organizationRepository.save(organization));
    }

    public OrganizationDetailedResponse getDetailedById(UUID id) {
        return organizationMapper.toDetailedResponse(findOrganizationAndValidateId(id));
    }

    public OrganizationSummaryResponse getSummaryById(UUID id) {
        return organizationMapper.toSummaryResponse(findOrganizationAndValidateId(id));
    }

    public Page<OrganizationSummaryResponse> findAll(FilteredSortedPaginatedRequest<OrganizationFilter, InvalidSortRule> request) {
        return organizationRepository.findAllFiltered(
                        request.getFilter().getName(),
                        request.getFilter().getTaxpayerIdNumber(),
                        request.getFilter().getEmail(),
                        request.getFilter().getStatus(),
                        request.getFilter().getProviderTypeId(),
                        request.getPageable()
                )
                .map(organizationMapper::toSummaryResponse);
    }

    public Map<String, Set<String>> getFiltersMap() {
        return Map.of(
                "status", Arrays.stream(Status.values()).map(Enum::name).collect(Collectors.toSet()),
                "providerTypeId", organizationRepository.findAllProviderTypeIds()
        );
    }

    @Transactional
    public void setOrganizationStatus(UUID organizationId, boolean activate) {
        findOrganizationAndValidateId(organizationId).setStatus(activate ? Status.ACTIVE : Status.INACTIVE);
        organizationRepository.save(findOrganizationAndValidateId(organizationId));
    }

    public UUID organizationIdOverride(HttpServletRequest request) {
        return request.getHeader("X-Organization-Id") != null ? UUID.fromString(request.getHeader("X-Organization-Id")) : null;
    }


    private Organization findOrganizationAndValidateId (UUID id) {
        return organizationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Organization not found"));
    }

    private ProviderType findProviderTypeAndValidateId (Long id) {
        return providerTypeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Provider type not found"));
    }
}
