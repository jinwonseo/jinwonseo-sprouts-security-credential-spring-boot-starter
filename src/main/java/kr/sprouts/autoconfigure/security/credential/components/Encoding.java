package kr.sprouts.autoconfigure.security.credential.components;

import java.util.Base64;

public enum Encoding {
    BASE64("Base64", Base64.getEncoder(), Base64.getDecoder()),
    BASE64URL("Base64Url", Base64.getUrlEncoder(), Base64.getUrlDecoder()),
    ;

    private final String name;
    private final Base64.Encoder encoder;
    private final Base64.Decoder decoder;

    Encoding(String name, Base64.Encoder encoder, Base64.Decoder decoder) {
        this.name = name;
        this.encoder = encoder;
        this.decoder = decoder;
    }

    public String getName() {
        return name;
    }

    public Base64.Encoder getEncoder() {
        return encoder;
    }

    public Base64.Decoder getDecoder() {
        return decoder;
    }

    public static Encoding nameOf(String name) {
        Encoding[] arr = values();

        for (Encoding encoding : arr) {
            if (encoding.getName().equalsIgnoreCase(name)) {
                return encoding;
            }
        }

        throw new IllegalArgumentException("Unsupported encoding '" + name + "'");
    }
}
