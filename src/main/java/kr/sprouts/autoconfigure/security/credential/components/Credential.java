package kr.sprouts.autoconfigure.security.credential.components;

public class Credential {
    private final Type type;
    private final String header;
    private final String value;

    private Credential(Credential.Type type, String header, String value) {
        this.type = type;
        this.header = header;
        this.value = type.prefix.equals("none") ? value : String.format("%s %s", type.prefix, value);
    }

    public static Credential of(Credential.Type type, String header, String value) {
        return new Credential(type, header, value);
    }

    public Type getType() {
        return type;
    }

    public String getHeader() {
        return header;
    }

    public String getValue() {
        return this.value.replace(this.type.getPrefix(), "").trim();
    }

    public enum Type {
        API_KEY("ApiKey", "none"),
        BEARER_TOKEN("BearerToken", "Bearer"),
        ;
        private final String name;

        private final String prefix;

        Type(String name, String prefix) {
            this.name = name;
            this.prefix = prefix;
        }

        public String getName() {
            return name;
        }

        public String getPrefix() {
            return prefix;
        }
    }
}
