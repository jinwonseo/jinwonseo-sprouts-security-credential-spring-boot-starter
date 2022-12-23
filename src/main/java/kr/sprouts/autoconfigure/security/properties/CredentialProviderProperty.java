package kr.sprouts.autoconfigure.security.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.UUID;

@ConfigurationProperties(prefix = "sprouts.security.credential-provider")
@Getter
@Setter
public class CredentialProviderProperty {
    private UUID id;
    private CredentialProperty credential;

    @Getter
    @Setter
    public static class CredentialProperty {
        private String type;
        private ApiKeyProperty apiKey;
        private BearerTokenProperty bearerToken;
    }

    @Getter
    @Setter
    public static class ApiKeyProperty {
        private String header;
        private KeyProperty key;
    }

    @Getter
    @Setter
    public static class BearerTokenProperty {
        private String header;
        private KeyProperty key;
    }

    @Getter
    @Setter
    public static class KeyProperty {
        private String algorithm;
        private String encodeType;
        private String encodedString;
    }
}
