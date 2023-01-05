package kr.sprouts.autoconfigure.security.credential.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "sprouts.security.credential")
public class CredentialProperty {
    private ApiKeyProperty apiKey;
    private BearerTokenProperty bearerToken;

    public ApiKeyProperty getApiKey() {
        return apiKey;
    }

    public void setApiKey(ApiKeyProperty apiKey) {
        this.apiKey = apiKey;
    }

    public BearerTokenProperty getBearerToken() {
        return bearerToken;
    }

    public void setBearerToken(BearerTokenProperty bearerToken) {
        this.bearerToken = bearerToken;
    }

    public static class ApiKeyProperty {
        private String header;
        private KeyProperty key;

        public String getHeader() {
            return header;
        }

        public void setHeader(String header) {
            this.header = header;
        }

        public KeyProperty getKey() {
            return key;
        }

        public void setKey(KeyProperty key) {
            this.key = key;
        }
    }

    public static class BearerTokenProperty {
        private String header;
        private KeyProperty key;

        public String getHeader() {
            return header;
        }

        public void setHeader(String header) {
            this.header = header;
        }

        public KeyProperty getKey() {
            return key;
        }

        public void setKey(KeyProperty key) {
            this.key = key;
        }
    }

    public static class KeyProperty {
        private String algorithm;
        private String encoding;
        private String encodedSecretKey;

        public String getAlgorithm() {
            return algorithm;
        }

        public void setAlgorithm(String algorithm) {
            this.algorithm = algorithm;
        }

        public String getEncoding() {
            return encoding;
        }

        public void setEncoding(String encoding) {
            this.encoding = encoding;
        }

        public String getEncodedSecretKey() {
            return encodedSecretKey;
        }

        public void setEncodedSecretKey(String encodedSecretKey) {
            this.encodedSecretKey = encodedSecretKey;
        }
    }
}
