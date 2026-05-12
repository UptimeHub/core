package uz.uptimehub.coreapp.mapper;

import org.mapstruct.*;
import uz.uptimehub.core.dto.organization.OrganizationCreateRequest;
import uz.uptimehub.core.dto.organization.OrganizationDetailedResponse;
import uz.uptimehub.core.dto.organization.OrganizationSummaryResponse;
import uz.uptimehub.core.dto.organization.OrganizationUpdateRequest;
import uz.uptimehub.core.dto.organizationadminassignment.OrganizationAdminAssignmentResponse;
import uz.uptimehub.coreapp.jpa.entity.Organization;
import uz.uptimehub.coreapp.jpa.entity.OrganizationAdminAssignment;
import uz.uptimehub.coreapp.jpa.entity.ProviderType;

@Mapper(componentModel = "spring")
public interface OrganizationMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "request.name")
    @Mapping(target = "taxpayerIdNumber", source = "request.taxpayerIdNumber")
    @Mapping(target = "email", source = "request.email")
    @Mapping(target = "status", constant = "PENDING")
    @Mapping(target = "providerType", expression = "java(providerType)")
    @Mapping(target = "organizationAdminAssignments", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Organization toOrganization(OrganizationCreateRequest request, ProviderType providerType);

    @Mapping(target = "providerTypeId", source = "providerType.id")
    OrganizationSummaryResponse toSummaryResponse(Organization organization);

    @Mapping(target = "providerTypeId", source = "providerType.id")
    OrganizationDetailedResponse toDetailedResponse(Organization organization);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "providerType", ignore = true)
    @Mapping(target = "organizationAdminAssignments", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateOrganization(OrganizationUpdateRequest request, @MappingTarget Organization organization);

    @Mapping(target = "organizationResponse", source = "organization")
    @Mapping(target = "keycloakUserId", source = "organizationAdminAssignment.keycloakUserId")
    @Mapping(target = "createdAt", source = "organizationAdminAssignment.createdAt")
    OrganizationAdminAssignmentResponse toOrganizationAdminAssignmentResponse(Organization organization, OrganizationAdminAssignment organizationAdminAssignment);
}
