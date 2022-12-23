package kr.sprouts.autoconfigure.security.components;

import kr.sprouts.autoconfigure.security.enumerations.CredentialType;

public final class BearerToken extends Credential {
    private BearerToken(String header, String value) {
        super(CredentialType.BEARER_TOKEN, header, value);
    }
    public static BearerToken of(String header, String value) {
        return new BearerToken(header, value);
    }
}
