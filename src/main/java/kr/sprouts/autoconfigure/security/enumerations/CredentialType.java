package kr.sprouts.autoconfigure.security.enumerations;

import io.jsonwebtoken.security.SignatureException;

public enum CredentialType {
    API_KEY("ApiKey"),
    BEARER_TOKEN("BearerToken"),
    ;

    private final String name;

    CredentialType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static CredentialType fromString(String string) {
        return CredentialType.valueOf(string.trim().toUpperCase());
    }

    public static CredentialType nameOf(String name) {
        CredentialType[] arr = values();

        for (CredentialType credentialType : arr) {
            if (credentialType.getName().equalsIgnoreCase(name)) {
                return credentialType;
            }
        }

        throw new SignatureException("Unsupported credential type '" + name + "'");
    }
}
