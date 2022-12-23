package kr.sprouts.autoconfigure.security.components;

import kr.sprouts.autoconfigure.security.enumerations.CredentialType;

public final class ApiKey extends Credential {
    private ApiKey(String header, String value) {
        super(CredentialType.API_KEY, header, value);
    }
    public static ApiKey of(String header, String value) {
        return new ApiKey(header, value);
    }
}
