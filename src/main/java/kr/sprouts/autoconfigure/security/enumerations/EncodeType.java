package kr.sprouts.autoconfigure.security.enumerations;

import java.util.Base64;

public enum EncodeType {
    BASE64("Base64", Base64.getEncoder(), Base64.getDecoder()),
    BASE64URL("Base64Url", Base64.getUrlEncoder(), Base64.getUrlDecoder()),
    ;

    private final String name;
    private final Base64.Encoder encoder;
    private final Base64.Decoder decoder;

    EncodeType(String name, Base64.Encoder encoder, Base64.Decoder decoder) {
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

    public static EncodeType nameOf(String name) {
        EncodeType[] arr = values();

        for (EncodeType encodeType : arr) {
            if (encodeType.getName().equalsIgnoreCase(name)) {
                return encodeType;
            }
        }

        throw new IllegalArgumentException("Unsupported encode type '" + name + "'");
    }
}
