package kr.sprouts.autoconfigure.security.components;

abstract class CredentialProvider {
    public abstract Credential provide(CredentialParam param);
}
