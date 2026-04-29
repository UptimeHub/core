package uz.uptimehub.coreapp.service;

import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uz.uptimehub.core.dto.organizationadminassignment.UserRole;
import uz.uptimehub.core.dto.user.UserResponse;
import uz.uptimehub.core.exception.EntityAlreadyExistsException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class KeycloakService {

    private final Keycloak keycloak;

    @Value("${keycloak.realm}")
    private String realm;
    @Value("${keycloak.client-id}")
    private String clientId;


    public String createAdmin(
            UUID organizationId,
            String username,
            String email,
            String firstName,
            String lastName,
            String password,
            UserRole role
    ) {
        assertRoleTypeAndOrganizationId(organizationId, role);
        RealmResource realmResource = keycloak.realm(realm);

        assertUserExists(realmResource, username, email);

        UserRepresentation user = new UserRepresentation();
        user.setUsername(username);
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmailVerified(true);
        user.setEnabled(true);

        if (organizationId != null)
            user.singleAttribute("organizationId", organizationId.toString());

        Response response = realmResource.users().create(user);

        if (response.getStatus() != 201) {
            throw new RuntimeException("Failed to create user in Keycloak: " + response.getStatusInfo());
        }

        String userId = CreatedResponseUtil.getCreatedId(response);

        setPassword(realmResource, userId, password);
        setRole(realmResource, userId, role);

        return userId;
    }

    public List<UserResponse> getAllAdminsByRoleType(UserRole role, UUID organizationId) {
        assertRoleTypeAndOrganizationId(organizationId, role);

        RealmResource realmResource = keycloak.realm(realm);

        return switch (role) {
            case ORGANIZATION_ADMIN -> realmResource.users()
                    .searchByAttributes("organizationId:" + organizationId)
                    .stream()
                    .map(user -> toUserResponse(user, role))
                    .toList();

            case PLATFORM_ADMIN -> realmResource.clients()
                    .findByClientId(clientId)
                    .stream()
                    .findFirst()
                    .map(client -> realmResource.clients()
                            .get(client.getId())
                            .roles()
                            .get(role.name())
                            .getUserMembers()
                            .stream()
                            .map(user -> toUserResponse(user, role))
                            .toList()
                    )
                    .orElse(List.of());
        };
    }

    private UserResponse toUserResponse(UserRepresentation user, UserRole role) {
        UUID organizationId = null;
        if (user.getAttributes() != null && user.getAttributes().containsKey("organizationId")) {
            organizationId = UUID.fromString(user.getAttributes().get("organizationId").getFirst());
        }

        return new UserResponse(
                UUID.fromString(user.getId()),
                user.getFirstName(),
                user.getLastName(),
                user.getUsername(),
                user.getEmail(),
                role.name(),
                organizationId
        );
    }

    private void assertRoleTypeAndOrganizationId(UUID organizationId, UserRole role) {
        if (organizationId != null && role != UserRole.ORGANIZATION_ADMIN)
            throw new BadRequestException("Organization id is required for organization admin role");
    }

    private void assertUserExists(RealmResource realmResource, String username, String email) {
        realmResource.users().search(username).stream().findFirst().ifPresent(_ -> {
            throw new EntityAlreadyExistsException("User already exists: " + username);
        });
        realmResource.users().search(email).stream().findFirst().ifPresent(_ -> {
            throw new EntityAlreadyExistsException("User already exists: " + email);
        });
    }

    private void setRole(RealmResource realmResource, String userId, UserRole roleName) {
        ClientRepresentation client = realmResource.clients()
                .findByClientId(clientId)
                .getFirst();

        RoleRepresentation role = realmResource.clients()
                .get(client.getId())
                .roles()
                .get(roleName.name())
                .toRepresentation();

        realmResource.users()
                .get(userId)
                .roles()
                .clientLevel(client.getId())
                .add(List.of(role));
    }

    private void setPassword(RealmResource realmResource, String userId, String password) {
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);
        credential.setTemporary(false);

        realmResource.users()
                .get(userId)
                .resetPassword(credential);
    }
}
