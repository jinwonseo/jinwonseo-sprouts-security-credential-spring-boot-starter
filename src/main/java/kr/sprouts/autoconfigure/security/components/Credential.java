package kr.sprouts.autoconfigure.security.components;

import kr.sprouts.autoconfigure.security.enumerations.CredentialType;

public abstract class Credential {
    private final CredentialType type;
    private final String header;
    private final String value;

    public Credential(CredentialType type, String header, String value) {
        this.type = type;
        this.header = header;
        this.value = value;
    }

    public CredentialType getType() {
        return type;
    }

    public String getHeader() {
        return header;
    }

    public String getValue() {
        return value;
    }
}
