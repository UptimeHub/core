package uz.uptimehub.coreapp.jpa.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "provider_type", indexes = {
        @Index(name = "provider_type_name_idx", columnList = "name")
})
public class ProviderType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "description", length = 500)
    private String description;
}
