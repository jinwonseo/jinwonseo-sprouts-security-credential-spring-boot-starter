package kr.sprouts.autoconfigure.security.credential.components;

import java.util.UUID;

public class ApiKeyParam extends CredentialParam {
    private ApiKeyParam(UUID providerId, UUID consumerId, UUID memberId) {
        super(providerId, consumerId, memberId);
    }

    public static ApiKeyParam of(UUID providerId, UUID consumerId, UUID memberId) {
        return new ApiKeyParam(providerId, consumerId, memberId);
    }
}

