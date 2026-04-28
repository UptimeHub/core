package uz.uptimehub.coreapp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import uz.uptimehub.core.dto.providertype.ProviderTypeCreateRequest;
import uz.uptimehub.core.dto.providertype.ProviderTypeDto;
import uz.uptimehub.coreapp.service.ProviderTypeService;

import java.util.List;

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
    public List<ProviderTypeDto> getAllProviderTypes() {
        return providerTypeService.getAll();
    }


}
