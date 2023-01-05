package kr.sprouts.autoconfigure.security.credential.components;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

abstract class CredentialProvider {
    private final String header;
    private final Algorithm algorithm;
    private final Encoding encoding;
    private final SecretKey secretKey;

    CredentialProvider(String header, Algorithm algorithm, Encoding encoding, String encodedSecretKey) {
        SecretKey secretKey = new SecretKeySpec(encoding.getDecoder().decode(encodedSecretKey), algorithm.getName());

        if (algorithm.getMinKeySize() > secretKey.getEncoded().length * 8) {
            throw new IllegalArgumentException("Not available secret key size");
        }

        this.header = header;
        this.algorithm = algorithm;
        this.encoding = encoding;
        this.secretKey = secretKey;
    }

    abstract Credential provide(CredentialParam param);

    String getHeader() {
        return header;
    }

    Algorithm getAlgorithm() {
        return algorithm;
    }

    Encoding getEncoding() {
        return encoding;
    }

    SecretKey getSecretKey() {
        return secretKey;
    }
}
