package kr.sprouts.autoconfigure.security.utilities;

import io.jsonwebtoken.security.Keys;
import kr.sprouts.autoconfigure.security.enumerations.AlgorithmType;
import kr.sprouts.autoconfigure.security.enumerations.CredentialType;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class CredentialUtility {
    public static SecretKey secretKeyFor(AlgorithmType algorithmType) {
        if (!algorithmType.getSupportKeyType().equals(SecretKey.class)) {
            throw new IllegalArgumentException("Unsupported algorithm type '" + algorithmType + "'");
        }

        if (Arrays.asList(algorithmType.getCredentialTypes()).contains(CredentialType.API_KEY)) {
            return CredentialUtility.generate(algorithmType);
        } else if (Arrays.asList(algorithmType.getCredentialTypes()).contains(CredentialType.BEARER_TOKEN)) {
            return Keys.secretKeyFor(algorithmType.getSignatureAlgorithm());
        }

        throw new IllegalArgumentException("Unsupported credential type");
    }

    public static KeyPair keyPairFor(AlgorithmType algorithmType) {
        if (!algorithmType.getSupportKeyType().equals(KeyPair.class)) {
            throw new IllegalArgumentException("Unsupported algorithm type '" + algorithmType + "'");
        }

        if (Arrays.asList(algorithmType.getCredentialTypes()).contains(CredentialType.BEARER_TOKEN)) {
            return Keys.keyPairFor(algorithmType.getSignatureAlgorithm());
        }

        throw new IllegalArgumentException("Unsupported credential type");
    }

    private static SecretKey generate(AlgorithmType algorithmType) {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(algorithmType.toString());
            keyGenerator.init(algorithmType.getMaxKeyLength());

            return keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
