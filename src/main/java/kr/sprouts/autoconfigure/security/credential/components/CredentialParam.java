package kr.sprouts.autoconfigure.security.credential.components;

import java.util.UUID;

abstract class CredentialParam {
    private final UUID providerId;
    private final UUID consumerId;
    private final UUID memberId;

    public CredentialParam(UUID providerId, UUID consumerId, UUID memberId) {
        this.providerId = providerId;
        this.consumerId = consumerId;
        this.memberId = memberId;
    }

    public UUID getProviderId() {
        return providerId;
    }

    public UUID getConsumerId() {
        return consumerId;
    }

    public UUID getMemberId() {
        return memberId;
    }
}
