package uz.uptimehub.coreapp.mapper;

import org.mapstruct.*;
import uz.uptimehub.core.dto.providertype.ProviderTypeCreateRequest;
import uz.uptimehub.core.dto.providertype.ProviderTypeDto;
import uz.uptimehub.coreapp.jpa.entity.ProviderType;

@Mapper(componentModel = "spring")
public interface ProviderTypeMapper {

    ProviderType toEntity(ProviderTypeCreateRequest request);
    ProviderTypeDto toDto(ProviderType entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    void updateEntity(ProviderTypeDto request, @MappingTarget ProviderType entity);
}
