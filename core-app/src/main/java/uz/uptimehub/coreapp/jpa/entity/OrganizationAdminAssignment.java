package uz.uptimehub.coreapp.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "organization_admin_assignment", indexes = {
        @Index(name = "organization_admin_assignment_organization_id_idx", columnList = "organization_id"),
        @Index(name = "organization_admin_assignment_keycloak_user_id_idx", columnList = "keycloak_user_id")
})
@EntityListeners(AuditingEntityListener.class)
public class OrganizationAdminAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", columnDefinition = "uuid")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    @Column(name = "keycloak_user_id", nullable = false)
    private UUID keycloakUserId;

    @CreatedDate
    private LocalDateTime createdAt;

}