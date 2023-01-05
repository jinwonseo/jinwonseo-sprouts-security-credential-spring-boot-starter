package kr.sprouts.autoconfigure.security.credential.components;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;

import java.util.UUID;

public class Principal {
    private UUID providerId;
    private UUID consumerId;
    private UUID memberId;

    private Principal() { }

    private Principal(UUID providerId, UUID consumerId, UUID memberId) {
        this.providerId = providerId;
        this.consumerId = consumerId;
        this.memberId = memberId;
    }

    public static Principal of(UUID providerId, UUID consumerId, UUID memberId) {
        return new Principal(providerId, consumerId, memberId);
    }

    public String toJson() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static Principal fromJson(String jsonString) {
        try {
            return new ObjectMapper().readValue(jsonString, Principal.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static Principal fromClaims(Claims claims) {
        return new Principal(
                UUID.fromString(claims.getIssuer()),
                UUID.fromString(claims.getAudience()),
                UUID.fromString(claims.getSubject())
        );
    }

    public static Principal fromCredentialParam(CredentialParam param) {
        return new Principal(param.getProviderId(), param.getConsumerId(), param.getMemberId());
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
