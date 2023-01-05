package kr.sprouts.autoconfigure.security.credential.components;

import org.junit.jupiter.api.Test;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class AlgorithmTest {
    @Test
    void test() {
        try {
            for (String name : Arrays.stream(Algorithm.values()).map(Algorithm::getName).collect(Collectors.toList())) {
                KeyGenerator keyGenerator = KeyGenerator.getInstance(name);
                keyGenerator.init(Algorithm.nameOf(name).getMinKeySize());
                SecretKey minSecretKey = keyGenerator.generateKey();
                assertThat(minSecretKey).isNotNull();

                keyGenerator.init(Algorithm.nameOf(name).getMaxKeySize());
                keyGenerator.generateKey();
                SecretKey maxSecretKey = keyGenerator.generateKey();
                assertThat(maxSecretKey).isNotNull();
            }
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}