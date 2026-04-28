package uz.uptimehub.coreapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.uptimehub.coreapi.dto.providertype.ProviderTypeCreateRequest;
import uz.uptimehub.coreapi.dto.providertype.ProviderTypeDto;
import uz.uptimehub.coreapi.exception.EntityNotFoundException;
import uz.uptimehub.coreapp.jpa.entity.ProviderType;
import uz.uptimehub.coreapp.jpa.repository.ProviderTypeRepository;
import uz.uptimehub.coreapp.mapper.ProviderTypeMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProviderTypeService {
    private final ProviderTypeRepository providerTypeRepository;
    private final ProviderTypeMapper providerTypeMapper;

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


    public List<ProviderTypeDto> getAll() {
        return providerTypeRepository.findAll().stream()
                .map(providerTypeMapper::toDto)
                .toList();
    }


    private ProviderType findProviderTypeAndValidateId (Long id) {
        return providerTypeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Provider type not found"));
    }
}
