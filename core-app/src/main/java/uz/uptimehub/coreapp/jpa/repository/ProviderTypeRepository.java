package uz.uptimehub.coreapp.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.uptimehub.coreapp.jpa.entity.ProviderType;

import java.util.Optional;

@Repository
public interface ProviderTypeRepository extends JpaRepository<ProviderType, Long> {
}