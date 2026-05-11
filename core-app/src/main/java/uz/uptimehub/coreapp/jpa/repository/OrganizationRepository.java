package uz.uptimehub.coreapp.jpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.uptimehub.core.dto.organization.Status;
import uz.uptimehub.coreapp.jpa.entity.Organization;

import java.util.Set;
import java.util.UUID;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, UUID> {


    @Query("select o.providerType.id from Organization o")
    Set<String> findAllProviderTypeIds();

    @Query("""
                select o from Organization o
                where (:name is null or :name = '' or lower(o.name) like lower(concat('%', :name, '%')))
                and (:taxpayerIdNumber is null or :taxpayerIdNumber = '' or lower(o.taxpayerIdNumber) like lower(concat('%', :taxpayerIdNumber, '%')))
                and (:email is null or :email = '' or lower(o.email) like lower(concat('%', :email, '%')))
                and (:status is null or o.status = :status)
                and (:providerTypeId is null or o.providerType.id = :providerTypeId)
   
""")
    Page<Organization> findAllFiltered(
            String name,
            String taxpayerIdNumber,
            String email,
            Status status,
            Long providerTypeId,
            Pageable pageable
    );
}
