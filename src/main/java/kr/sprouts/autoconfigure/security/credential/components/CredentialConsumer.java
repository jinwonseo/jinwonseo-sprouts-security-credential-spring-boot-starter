package kr.sprouts.autoconfigure.security.credential.components;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

abstract class CredentialConsumer {
    private final Algorithm algorithm;
    private final Encoding encoding;
    private final SecretKey secretKey;

    CredentialConsumer(Algorithm algorithm, Encoding encoding, String encodedSecretKey) {
        SecretKey secretKey = new SecretKeySpec(encoding.getDecoder().decode(encodedSecretKey), algorithm.getName());

        if (algorithm.getMinKeySize() > secretKey.getEncoded().length * 8) {
            throw new IllegalArgumentException("Not available secret key size");
        }

        this.algorithm = algorithm;
        this.encoding = encoding;
        this.secretKey = secretKey;
    }

    abstract Principal consume(String credentialValue);

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
