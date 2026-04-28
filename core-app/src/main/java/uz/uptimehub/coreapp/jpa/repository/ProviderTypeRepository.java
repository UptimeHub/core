package uz.uptimehub.coreapp.jpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.uptimehub.coreapp.jpa.entity.ProviderType;

@Repository
public interface ProviderTypeRepository extends JpaRepository<ProviderType, Long> {

    @Query("""
                select pt from ProviderType pt
                where (:name is null or :name = '' or lower(pt.name) like lower(concat('%', :name, '%')))
            """)
    Page<ProviderType> findAllFiltered(
            @Param("name") String name,
            Pageable pageable
    );
}