package uz.uptimehub.coreapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import uz.uptimehub.core.dto.providertype.ProviderTypeCreateRequest;
import uz.uptimehub.core.dto.providertype.ProviderTypeDto;
import uz.uptimehub.core.dto.providertype.ProviderTypeFilter;
import uz.uptimehub.core.exception.EntityNotFoundException;
import uz.uptimehub.core.exception.InvalidSortRule;
import uz.uptimehub.core.pagination.FilteredSortedPaginatedRequest;
import uz.uptimehub.coreapp.jpa.entity.ProviderType;
import uz.uptimehub.coreapp.jpa.repository.ProviderTypeRepository;
import uz.uptimehub.coreapp.mapper.ProviderTypeMapper;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProviderTypeService {
    private final ProviderTypeRepository providerTypeRepository;
    private final ProviderTypeMapper providerTypeMapper;

    private static final Map<String, String> FILTER_PROPERTY_QUERIES = new LinkedHashMap<>();

    static {
        FILTER_PROPERTY_QUERIES.put(
                "name",
                ":name is null or :name = '' or lower(pt.name) like lower(concat('%', :name, '%'))"
        );
    }

    public ProviderTypeDto save(ProviderTypeCreateRequest request) {
        return providerTypeMapper.toDto(providerTypeRepository.save(providerTypeMapper.toEntity(request)));
    }

    public ProviderTypeDto update(ProviderTypeDto updateRequest) {
        ProviderType providerType = findProviderTypeAndValidateId(updateRequest.id());

        providerTypeMapper.updateEntity(updateRequest, providerType);

        return providerTypeMapper.toDto(providerTypeRepository.save(providerType));
    }

    public ProviderTypeDto getById(Long id) {
        return providerTypeMapper.toDto(findProviderTypeAndValidateId(id));
    }


    public Page<ProviderTypeDto> getAll(FilteredSortedPaginatedRequest<ProviderTypeFilter, InvalidSortRule> request) {
        return providerTypeRepository.findAllFiltered(request.getFilter().getName(), request.getPageable())
                .map(providerTypeMapper::toDto);
    }

    private ProviderType findProviderTypeAndValidateId (Long id) {
        return providerTypeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Provider type not found"));
    }
}
