package uz.uptimehub.coreapp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.uptimehub.coreapp.service.OrganizationAdminService;

@RestController
@RequestMapping("/api/organization-admin/")
@RequiredArgsConstructor
public class OrganizationAdminController {
    private final OrganizationAdminService organizationAdminService;
}
