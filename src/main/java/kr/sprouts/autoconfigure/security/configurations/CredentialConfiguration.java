package kr.sprouts.autoconfigure.security.configurations;

import kr.sprouts.autoconfigure.security.components.ApiKeyProvider;
import kr.sprouts.autoconfigure.security.components.BearerTokenProvider;
import kr.sprouts.autoconfigure.security.enumerations.AlgorithmType;
import kr.sprouts.autoconfigure.security.enumerations.EncodeType;
import kr.sprouts.autoconfigure.security.properties.CredentialProperty;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(value = { CredentialProperty.class })
public class CredentialConfiguration {

    private final CredentialProperty credentialProperty;

    public CredentialConfiguration(CredentialProperty credentialProperty) {
        this.credentialProperty = credentialProperty;

        LoggerFactory.getLogger(CredentialConfiguration.class)
                .info(String.format("Initialized %s", CredentialConfiguration.class.getName()));
    }

    @Bean
    public ApiKeyProvider apiKeyProvider() {
        return ApiKeyProvider.of(
                this.credentialProperty.getApiKey().getHeader(),
                AlgorithmType.valueOf(this.credentialProperty.getApiKey().getKey().getAlgorithm()),
                EncodeType.nameOf(this.credentialProperty.getApiKey().getKey().getEncodeType()),
                this.credentialProperty.getApiKey().getKey().getEncodedString()
        );
    }

    @Bean
    public BearerTokenProvider bearerTokenProvider() {
        return BearerTokenProvider.of(
                this.credentialProperty.getBearerToken().getHeader(),
                AlgorithmType.valueOf(this.credentialProperty.getBearerToken().getKey().getAlgorithm()),
                EncodeType.nameOf(this.credentialProperty.getBearerToken().getKey().getEncodeType()),
                this.credentialProperty.getBearerToken().getKey().getEncodedString()
        );
    }
}
