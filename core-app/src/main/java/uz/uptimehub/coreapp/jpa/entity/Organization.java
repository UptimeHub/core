package uz.uptimehub.coreapp.jpa.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import uz.uptimehub.core.dto.organization.Status;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
@Table(name = "organization", indexes = {
        @Index(name = "organization_name_idx", columnList = "name"),
        @Index(name = "organization_taxpayer_id_number_idx", columnList = "taxpayer_id_number"),
        @Index(name = "organization_email_idx", columnList = "email"),
        @Index(name = "organization_status_idx", columnList = "status"),
        @Index(name = "organization_provider_type_id_idx", columnList = "provider_type_id")
})
public class Organization {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", columnDefinition = "uuid")
    private UUID id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "taxpayer_id_number", nullable = false, unique = true)
    private String taxpayerIdNumber;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "provider_type_id", nullable = false)
    private ProviderType providerType;

    @OneToMany(mappedBy = "organization", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("createdAt DESC")
    private List<OrganizationAdminAssignment> organizationAdminAssignments;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

}
