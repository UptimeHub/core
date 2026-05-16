package uz.uptimehub.coreapp.service;

import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
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

@Slf4j
@Service
@RequiredArgsConstructor
public class KeycloakService {

    private final Keycloak keycloak;
    private static final String ORGANIZATION_ID_ATTRIBUTE = "organization_id";

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

        if (role == UserRole.ORGANIZATION_ADMIN) {
            user.singleAttribute(ORGANIZATION_ID_ATTRIBUTE, organizationId.toString());
        }

        Response response = realmResource.users().create(user);

        if (response.getStatus() != 201) {
            throw new RuntimeException("Failed to create user in Keycloak: " + response.getStatusInfo());
        }

        String userId = CreatedResponseUtil.getCreatedId(response);

        setPassword(realmResource, userId, password);
        assignRealmRole(realmResource, userId, role.name());

        return userId;
    }

    public List<UserResponse> getAllAdminsByRoleType(UserRole role, UUID organizationId) {
        assertRoleTypeAndOrganizationId(organizationId, role);

        RealmResource realmResource = keycloak.realm(realm);

        return switch (role) {
            case ORGANIZATION_ADMIN -> realmResource.users()
                    .searchByAttributes(ORGANIZATION_ID_ATTRIBUTE + ":" + organizationId)
                    .stream()
                    .filter(user -> hasRealmRole(realmResource, user.getId(), UserRole.ORGANIZATION_ADMIN.name()))
                    .map(user -> toUserResponse(user, role))
                    .toList();

            case PLATFORM_ADMIN -> realmResource.roles()
                    .get(UserRole.PLATFORM_ADMIN.name())
                    .getUserMembers()
                    .stream()
                    .map(user -> toUserResponse(user, role))
                    .toList();
        };
    }

    private void assignRealmRole(
            RealmResource realmResource,
            String userId,
            String roleName
    ) {
        RoleRepresentation role = realmResource.roles()
                .get(roleName)
                .toRepresentation();

        realmResource.users()
                .get(userId)
                .roles()
                .realmLevel()
                .add(List.of(role));
    }

    private boolean hasRealmRole(
            RealmResource realmResource,
            String userId,
            String roleName
    ) {
        return realmResource.users()
                .get(userId)
                .roles()
                .realmLevel()
                .listEffective()
                .stream()
                .anyMatch(role -> roleName.equals(role.getName()));
    }

    private UserResponse toUserResponse(UserRepresentation user, UserRole role) {
        UUID organizationId = null;
        if (user.getAttributes() != null) {
            List<String> organizationIds = user.getAttributes().get(ORGANIZATION_ID_ATTRIBUTE);
            if (organizationIds != null && !organizationIds.isEmpty()) {
                organizationId = UUID.fromString(organizationIds.getFirst());
            }
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
        if ((organizationId == null && role == UserRole.ORGANIZATION_ADMIN) ||
                (organizationId != null && role == UserRole.PLATFORM_ADMIN))
            throw new BadRequestException("Organization id is required only for organization admin role");
    }

    private void assertUserExists(RealmResource realmResource, String username, String email) {
        realmResource.users().search(username).stream().findFirst().ifPresent(_ -> {
            throw new EntityAlreadyExistsException("User already exists: " + username);
        });
        realmResource.users().search(email).stream().findFirst().ifPresent(_ -> {
            throw new EntityAlreadyExistsException("User already exists: " + email);
        });
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
