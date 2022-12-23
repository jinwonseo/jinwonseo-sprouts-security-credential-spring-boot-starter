package kr.sprouts.autoconfigure.security.configurations;

import kr.sprouts.autoconfigure.security.components.ApiKeyProvider;
import kr.sprouts.autoconfigure.security.components.BearerTokenProvider;
import kr.sprouts.autoconfigure.security.components.CredentialProvider;
import kr.sprouts.autoconfigure.security.enumerations.AlgorithmType;
import kr.sprouts.autoconfigure.security.enumerations.CredentialType;
import kr.sprouts.autoconfigure.security.enumerations.EncodeType;
import kr.sprouts.autoconfigure.security.properties.CredentialProviderProperty;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(value = { CredentialProviderProperty.class })
public class CredentialProviderConfiguration {

    private final CredentialProviderProperty credentialProviderProperty;

    public CredentialProviderConfiguration(CredentialProviderProperty credentialProviderProperty) {
        this.credentialProviderProperty = credentialProviderProperty;

        LoggerFactory.getLogger(CredentialProviderConfiguration.class)
                .info(String.format("Initialized %s", CredentialProviderConfiguration.class.getName()));
    }

    @Bean
    public CredentialProvider credentialProvider() {
        CredentialType credentialType = CredentialType.nameOf(this.credentialProviderProperty.getCredential().getType());

        switch (credentialType) {
            case API_KEY:
                return ApiKeyProvider.of(
                        this.credentialProviderProperty.getId(),
                        this.credentialProviderProperty.getCredential().getApiKey().getHeader(),
                        AlgorithmType.valueOf(this.credentialProviderProperty.getCredential().getApiKey().getKey().getAlgorithm()),
                        EncodeType.nameOf(this.credentialProviderProperty.getCredential().getApiKey().getKey().getEncodeType()),
                        this.credentialProviderProperty.getCredential().getApiKey().getKey().getEncodedString()
                );

            case BEARER_TOKEN:
                return BearerTokenProvider.of(
                        this.credentialProviderProperty.getId(),
                        this.credentialProviderProperty.getCredential().getBearerToken().getHeader(),
                        AlgorithmType.valueOf(this.credentialProviderProperty.getCredential().getBearerToken().getKey().getAlgorithm()),
                        EncodeType.nameOf(this.credentialProviderProperty.getCredential().getBearerToken().getKey().getEncodeType()),
                        this.credentialProviderProperty.getCredential().getBearerToken().getKey().getEncodedString()
                );

            default:
                throw new IllegalArgumentException("Unsupported credential type '" + credentialType + "'");
        }
    }
}
