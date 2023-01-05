package kr.sprouts.autoconfigure.security.credential.components;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class ApiKeyConsumer extends CredentialConsumer {
    private ApiKeyConsumer(Algorithm algorithm, Encoding encoding, String encodedSecretKey) {
        super(algorithm, encoding, encodedSecretKey);
    }

    public static ApiKeyConsumer of(Algorithm algorithm, Encoding encoding, String encodedSecretKey) {
        return new ApiKeyConsumer(algorithm, encoding, encodedSecretKey);
    }

    @Override
    public Principal consume(String encodedCredentialValue) {
        return Principal.fromJson(new String(this.decrypt(getEncoding().getDecoder().decode(encodedCredentialValue))));
    }

    private byte[] decrypt(byte[] encryptedValue) {
        try {
            Cipher cipher = Cipher.getInstance(getAlgorithm().getName());
            cipher.init(Cipher.DECRYPT_MODE, getSecretKey());

            return cipher.doFinal(encryptedValue);
        } catch (NoSuchPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException | BadPaddingException |
                 InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }
}
