package kr.sprouts.autoconfigure.security.components;

import kr.sprouts.autoconfigure.security.enumerations.AlgorithmType;
import kr.sprouts.autoconfigure.security.enumerations.CredentialType;
import kr.sprouts.autoconfigure.security.enumerations.EncodeType;
import lombok.Getter;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

@Getter
public class ApiKeyProvider extends CredentialProvider {
    private final String header;
    private final AlgorithmType algorithmType;
    private final EncodeType encodeType;
    private final SecretKey secretKey;

    private ApiKeyProvider(String header, AlgorithmType algorithmType, EncodeType encodeType, String encodedSecretKeyString) {
        if (!Arrays.asList(algorithmType.getCredentialTypes()).contains(CredentialType.API_KEY)) {
            throw new IllegalArgumentException("Not available algorithm");
        }

        this.header = header;
        this.algorithmType = algorithmType;
        this.encodeType = encodeType;

        SecretKey secretKey = this.convertToSecretKey(encodedSecretKeyString);

        if (algorithmType.getMinKeyLength() > secretKey.getEncoded().length * 8) {
            throw new IllegalArgumentException("Not available secret key size");
        }

        this.secretKey = secretKey;
    }

    public static ApiKeyProvider of(String header, AlgorithmType algorithmType, EncodeType encodeType, String encodedSecretKeyString) {
        return new ApiKeyProvider(header, algorithmType, encodeType, encodedSecretKeyString);
    }

    @Override
    public ApiKey provide(CredentialParam param) {
        if (!(param instanceof ApiKeyParam)) {
            throw new IllegalArgumentException("Unexpected parameter " + param.getClass().getSimpleName());
        }

        return ApiKey.of(this.header, this.encode(this.encrypt(String.format("%s/%s/%s",
                ((ApiKeyParam) param).getProviderId(),
                ((ApiKeyParam) param).getMemberId(),
                ((ApiKeyParam) param).getClientId()
        ))));
    }

    public String consume(String encodedAndEncryptedText) {
        return new String(this.decrypt(this.decode(encodedAndEncryptedText)));
    }

    private byte[] encrypt(String plainText) {
        try {
            Cipher cipher = Cipher.getInstance(this.algorithmType.toString());
            cipher.init(Cipher.ENCRYPT_MODE, this.secretKey);

            return cipher.doFinal(plainText.getBytes());
        } catch (NoSuchPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException | BadPaddingException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

    private byte[] decrypt(byte[] encryptedValue) {
        try {
            Cipher cipher = Cipher.getInstance(this.algorithmType.toString());
            cipher.init(Cipher.DECRYPT_MODE, this.secretKey);

            return cipher.doFinal(encryptedValue);
        } catch (NoSuchPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException | BadPaddingException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

    private String encode(byte[] encryptedValue) {
        return this.encodeType.getEncoder().encodeToString(encryptedValue);
    }

    private byte[] decode(String encodedValue) {
        return this.encodeType.getDecoder().decode(encodedValue);
    }

    private SecretKey convertToSecretKey(String encodedSecretKeyString) {
        return new SecretKeySpec(this.decode(encodedSecretKeyString), this.algorithmType.toString());
    }
}
