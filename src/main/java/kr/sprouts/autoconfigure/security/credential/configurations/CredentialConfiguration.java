package kr.sprouts.autoconfigure.security.credential.configurations;

import kr.sprouts.autoconfigure.security.credential.components.Algorithm;
import kr.sprouts.autoconfigure.security.credential.components.ApiKeyConsumer;
import kr.sprouts.autoconfigure.security.credential.components.ApiKeyProvider;
import kr.sprouts.autoconfigure.security.credential.components.BearerTokenConsumer;
import kr.sprouts.autoconfigure.security.credential.components.BearerTokenProvider;
import kr.sprouts.autoconfigure.security.credential.components.Encoding;
import kr.sprouts.autoconfigure.security.credential.properties.CredentialProperty;

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
                Algorithm.nameOf(this.credentialProperty.getApiKey().getKey().getAlgorithm()),
                Encoding.nameOf(this.credentialProperty.getApiKey().getKey().getEncoding()),
                this.credentialProperty.getApiKey().getKey().getEncodedSecretKey()
        );
    }

    @Bean
    public ApiKeyConsumer apiKeyConsumer() {
        return ApiKeyConsumer.of(
                Algorithm.nameOf(this.credentialProperty.getApiKey().getKey().getAlgorithm()),
                Encoding.nameOf(this.credentialProperty.getApiKey().getKey().getEncoding()),
                this.credentialProperty.getApiKey().getKey().getEncodedSecretKey()
        );
    }

    @Bean
    public BearerTokenProvider bearerTokenProvider() {
        return BearerTokenProvider.of(
                this.credentialProperty.getBearerToken().getHeader(),
                Algorithm.nameOf(this.credentialProperty.getBearerToken().getKey().getAlgorithm()),
                Encoding.nameOf(this.credentialProperty.getBearerToken().getKey().getEncoding()),
                this.credentialProperty.getBearerToken().getKey().getEncodedSecretKey()
        );
    }

    @Bean
    public BearerTokenConsumer bearerTokenConsumer() {
        return BearerTokenConsumer.of(Algorithm.nameOf(this.credentialProperty.getBearerToken().getKey().getAlgorithm()),
                Encoding.nameOf(this.credentialProperty.getBearerToken().getKey().getEncoding()),
                this.credentialProperty.getBearerToken().getKey().getEncodedSecretKey());
    }
}
