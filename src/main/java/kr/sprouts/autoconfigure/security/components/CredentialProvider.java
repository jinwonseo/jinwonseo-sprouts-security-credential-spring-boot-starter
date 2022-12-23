package kr.sprouts.autoconfigure.security.components;

public abstract class CredentialProvider {
    public abstract Credential provide(CredentialParam param);
}
