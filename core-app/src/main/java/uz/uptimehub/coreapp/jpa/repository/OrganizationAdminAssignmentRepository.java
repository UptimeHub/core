package uz.uptimehub.coreapp.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.uptimehub.coreapp.jpa.entity.OrganizationAdminAssignment;

import java.util.UUID;

@Repository
public interface OrganizationAdminAssignmentRepository extends JpaRepository<OrganizationAdminAssignment, UUID> {
}