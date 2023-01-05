package kr.sprouts.autoconfigure.security.credential.components;

import java.util.UUID;

public class BearerTokenParam extends CredentialParam {
    private final Long validityInSeconds;

    private BearerTokenParam(UUID providerId, UUID consumerId, UUID memberId, Long validityInSeconds) {
        super(providerId, consumerId, memberId);
        this.validityInSeconds = validityInSeconds;
    }

    public static BearerTokenParam of(UUID providerId, UUID consumerId, UUID memberId, Long validityInSeconds) {
        return new BearerTokenParam(providerId, consumerId, memberId, validityInSeconds);
    }

    public Long getValidityInSeconds() {
        return validityInSeconds;
    }
}
