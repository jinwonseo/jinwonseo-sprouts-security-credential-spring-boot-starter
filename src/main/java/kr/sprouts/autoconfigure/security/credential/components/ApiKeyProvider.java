package kr.sprouts.autoconfigure.security.credential.components;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class ApiKeyProvider extends CredentialProvider {
    public ApiKeyProvider(String header, Algorithm algorithm, Encoding encoding, String encodedSecretKey) {
        super(header, algorithm, encoding, encodedSecretKey);
    }

    public static ApiKeyProvider of(String header, Algorithm algorithm, Encoding encoding, String encodedSecretKey) {
        return new ApiKeyProvider(header, algorithm, encoding, encodedSecretKey);
    }

    @Override
    public Credential provide(CredentialParam param) {
        if (!(param instanceof ApiKeyParam)) {
            throw new IllegalArgumentException("Unexpected parameter " + param.getClass().getSimpleName());
        }

        return Credential.of(
                Credential.Type.API_KEY,
                this.getHeader(),
                this.getEncoding().getEncoder().encodeToString(this.encrypt(Principal.fromCredentialParam(param).toJson()))
        );
    }

    private byte[] encrypt(String plainText) {
        try {
            Cipher cipher = Cipher.getInstance(this.getAlgorithm().toString());
            cipher.init(Cipher.ENCRYPT_MODE, this.getSecretKey());

            return cipher.doFinal(plainText.getBytes());
        } catch (NoSuchPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException | BadPaddingException |
                 InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }
}
