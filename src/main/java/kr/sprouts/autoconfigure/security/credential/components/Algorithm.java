package kr.sprouts.autoconfigure.security.credential.components;

public enum Algorithm {
    AES("AES", new Credential.Type[]{ Credential.Type.API_KEY }, 128, 256),
    ARCFOUR("ARCFOUR", new Credential.Type[]{ Credential.Type.API_KEY }, 40, 1024),
    Blowfish("Blowfish", new Credential.Type[]{ Credential.Type.API_KEY }, 32, 448),
    DES("DES", new Credential.Type[]{ Credential.Type.API_KEY }, 56, 56),
    DESede("DESede", new Credential.Type[]{ Credential.Type.API_KEY }, 112, 168),
    HmacMD5("HmacMD5", new Credential.Type[]{ Credential.Type.API_KEY }, 128),
    HmacSHA1("HmacSHA1", new Credential.Type[]{ Credential.Type.API_KEY }, 128),
    HmacSHA224("HmacSHA224", new Credential.Type[]{ Credential.Type.API_KEY }, 224),
    HmacSHA256("HmacSHA256", "HS256", new Credential.Type[]{ Credential.Type.API_KEY, Credential.Type.BEARER_TOKEN }, 256),
    HmacSHA384("HmacSHA384", "HS384", new Credential.Type[]{ Credential.Type.API_KEY, Credential.Type.BEARER_TOKEN }, 384),
    HmacSHA512("HmacSHA512", "HS512", new Credential.Type[]{ Credential.Type.API_KEY, Credential.Type.BEARER_TOKEN }, 512),
    RC2("RC2", new Credential.Type[]{ Credential.Type.API_KEY }, 40, 1024),
    ;

    public final Integer MAX_KEY_SIZE = 8196;

    /**
     * Java security standard algorithm name
     */
    private final String name;
    private final String simpleName;
    private final Credential.Type[] supportedCredentials;
    private final Integer minKeySize;
    private final Integer maxKeySize;

    Algorithm(String name, Credential.Type[] supportedCredentials, Integer minKeySize, Integer maxKeySize) {
        this.name = name;
        this.simpleName = name;
        this.supportedCredentials = supportedCredentials;
        this.minKeySize = minKeySize;
        this.maxKeySize = maxKeySize;
    }

    Algorithm(String name, Credential.Type[] supportedCredentials, Integer minKeySize) {
        this.name = name;
        this.simpleName = name;
        this.supportedCredentials = supportedCredentials;
        this.minKeySize = minKeySize;
        this.maxKeySize = MAX_KEY_SIZE;
    }

    Algorithm(String name, String simpleName, Credential.Type[] supportedCredentials, Integer minKeySize) {
        this.name = name;
        this.simpleName = simpleName;
        this.supportedCredentials = supportedCredentials;
        this.minKeySize = minKeySize;
        this.maxKeySize = MAX_KEY_SIZE;
    }

    public static Algorithm nameOf(String name) {
        Algorithm[] arr = values();

        for (Algorithm algorithm : arr) {
            if (algorithm.getName().equalsIgnoreCase(name)) {
                return algorithm;
            }
        }

        throw new IllegalArgumentException("Unsupported algorithm '" + name + "'");
    }

    public String getName() {
        return name;
    }

    public String getSimpleName() {
        return simpleName;
    }

    public Credential.Type[] getSupportedCredentials() {
        return supportedCredentials;
    }

    public Integer getMinKeySize() {
        return minKeySize;
    }

    public Integer getMaxKeySize() {
        return maxKeySize;
    }
}
