package uz.uptimehub.coreapp.service;

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

    private static final String ORG_ADMIN_ROLE_NAME = "ORGANIZATION_ADMIN";

    public String createOrganizationAdmin(
            UUID organizationId,
            String username,
            String email,
            String firstName,
            String lastName,
            String password
    ) {
        RealmResource realmResource = keycloak.realm(realm);

        UserRepresentation user = new UserRepresentation();
        user.setUsername(username);
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmailVerified(true);
        user.setEnabled(true);

        user.singleAttribute("organizationId", organizationId.toString());
        Response response = realmResource.users().create(user);

        if (response.getStatus() != 201) {
            throw new RuntimeException("Failed to create user in Keycloak: " + response.getStatusInfo());
        }

        String userId = CreatedResponseUtil.getCreatedId(response);

        setPassword(realmResource, userId, password);
        setRole(realmResource, userId);

        return userId;
    }

    private void setRole(RealmResource realmResource, String userId) {
        ClientRepresentation client = realmResource.clients()
                .findByClientId(clientId)
                .getFirst();

        RoleRepresentation role = realmResource.clients()
                .get(client.getId())
                .roles()
                .get(ORG_ADMIN_ROLE_NAME)
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
