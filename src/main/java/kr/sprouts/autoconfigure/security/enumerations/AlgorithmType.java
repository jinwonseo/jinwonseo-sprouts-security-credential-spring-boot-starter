package kr.sprouts.autoconfigure.security.enumerations;

import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.SecretKey;
import java.lang.reflect.Type;
import java.security.KeyPair;

public enum AlgorithmType {
    AES(new CredentialType[]{ CredentialType.API_KEY }, 128, 256, SecretKey.class),
    ARCFOUR(new CredentialType[]{ CredentialType.API_KEY }, 40, 1024, SecretKey.class),
    CHACHA20(new CredentialType[]{ CredentialType.API_KEY }, 256, 256, SecretKey.class),
    DES(new CredentialType[]{ CredentialType.API_KEY }, 56, 56, SecretKey.class),
    DESEDE(new CredentialType[]{ CredentialType.API_KEY }, 112, 168, SecretKey.class),
    HMACSHA1(new CredentialType[]{ CredentialType.API_KEY }, 160, 160, SecretKey.class),
    HS256(new CredentialType[]{ CredentialType.API_KEY, CredentialType.BEARER_TOKEN }, SignatureAlgorithm.HS256, SecretKey.class),
    HS384(new CredentialType[]{ CredentialType.API_KEY, CredentialType.BEARER_TOKEN }, SignatureAlgorithm.HS384, SecretKey.class),
    HS512(new CredentialType[]{ CredentialType.API_KEY, CredentialType.BEARER_TOKEN }, SignatureAlgorithm.HS512, SecretKey.class),
    RS256(new CredentialType[]{ CredentialType.BEARER_TOKEN }, SignatureAlgorithm.RS256, KeyPair.class),
    RS384(new CredentialType[]{ CredentialType.BEARER_TOKEN }, SignatureAlgorithm.RS384, KeyPair.class),
    RS512(new CredentialType[]{ CredentialType.BEARER_TOKEN }, SignatureAlgorithm.RS512, KeyPair.class),
    ES256(new CredentialType[]{ CredentialType.BEARER_TOKEN }, SignatureAlgorithm.ES256, KeyPair.class),
    ES384(new CredentialType[]{ CredentialType.BEARER_TOKEN }, SignatureAlgorithm.ES384, KeyPair.class),
    ES512(new CredentialType[]{ CredentialType.BEARER_TOKEN }, SignatureAlgorithm.ES512, KeyPair.class),
    PS256(new CredentialType[]{ CredentialType.BEARER_TOKEN }, SignatureAlgorithm.PS256, KeyPair.class),
    PS384(new CredentialType[]{ CredentialType.BEARER_TOKEN }, SignatureAlgorithm.PS384, KeyPair.class),
    PS512(new CredentialType[]{ CredentialType.BEARER_TOKEN }, SignatureAlgorithm.PS512, KeyPair.class),
    ;

    private final CredentialType[] credentialTypes;
    private final int minKeyLength;
    private int maxKeyLength;
    private final Type supportKeyType;
    private SignatureAlgorithm signatureAlgorithm;

    AlgorithmType(CredentialType[] credentialTypes, int minKeyLength, int maxKeyLength, Type supportKeyType) {
        this.credentialTypes = credentialTypes;
        this.minKeyLength = minKeyLength;
        this.maxKeyLength = maxKeyLength;
        this.supportKeyType = supportKeyType;
    }

    AlgorithmType(CredentialType[] credentialTypes, SignatureAlgorithm signatureAlgorithm, Type supportKeyType) {
        this.credentialTypes = credentialTypes;
        this.minKeyLength = signatureAlgorithm.getMinKeyLength();
        this.signatureAlgorithm = signatureAlgorithm;
        this.supportKeyType = supportKeyType;
    }

    public CredentialType[] getCredentialTypes() {
        return credentialTypes;
    }

    public int getMinKeyLength() {
        return minKeyLength;
    }

    public int getMaxKeyLength() {
        return maxKeyLength;
    }

    public Type getSupportKeyType() {
        return supportKeyType;
    }

    public SignatureAlgorithm getSignatureAlgorithm() {
        return signatureAlgorithm;
    }
}
