package kr.sprouts.autoconfigure.security.configurations;

import io.jsonwebtoken.Claims;
import kr.sprouts.autoconfigure.security.components.ApiKeyParam;
import kr.sprouts.autoconfigure.security.components.ApiKeyProvider;
import kr.sprouts.autoconfigure.security.components.BearerTokenParam;
import kr.sprouts.autoconfigure.security.components.BearerTokenProvider;
import kr.sprouts.autoconfigure.security.components.Credential;
import kr.sprouts.autoconfigure.security.enumerations.AlgorithmType;
import kr.sprouts.autoconfigure.security.properties.CredentialProviderProperty;
import kr.sprouts.autoconfigure.security.utilities.CredentialProviderUtility;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import java.util.Base64;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class CredentialProviderConfigurationTest {
    private final ApplicationContextRunner applicationContextRunner = new ApplicationContextRunner().withConfiguration(AutoConfigurations.of(
            CredentialProviderConfiguration.class
    ));

    @Test
    void generate_key() {
        log.info(Base64.getUrlEncoder().encodeToString(CredentialProviderUtility.secretKeyFor(AlgorithmType.AES).getEncoded()));
        log.info(Base64.getUrlEncoder().encodeToString(CredentialProviderUtility.keyPairFor(AlgorithmType.ES256).getPrivate().getEncoded()));
    }

    @Test
    void bean_test_apiKey() {
        String[] properties = {
                "sprouts.security.credential-provider.id=333ef2a7-2a43-43f3-9222-b7798a3a2cdc",
                "sprouts.security.credential-provider.credential.type=ApiKey",
                "sprouts.security.credential-provider.credential.api-key.header=API-KEY",
                "sprouts.security.credential-provider.credential.api-key.key.algorithm=AES",
                "sprouts.security.credential-provider.credential.api-key.key.encode-type=BASE64URL",
                "sprouts.security.credential-provider.credential.api-key.key.encoded-string=5150xqtD36SDH01oeBGQmcZifuvS0W_RncdGDAhMui4=",
        };

        this.applicationContextRunner.withPropertyValues(properties).run(context -> {
            assertThat(context).hasSingleBean(CredentialProviderConfiguration.class);
            assertThat(context).hasSingleBean(CredentialProviderProperty.class);
            assertThat(context).hasSingleBean(ApiKeyProvider.class);

            String providerId = context.getBean(CredentialProviderProperty.class).getId().toString();
            UUID memberId = UUID.randomUUID();
            UUID clientId = UUID.randomUUID();
            String originText = String.format("%s/%s/%s", providerId, memberId, clientId);

            Credential credential = context.getBean(ApiKeyProvider.class).provide(ApiKeyParam.of(memberId, clientId));
            assertThat(context.getBean(ApiKeyProvider.class).consume(credential.getValue()).equals(originText)).isTrue();
        });
    }

    @Test
    void bean_test_bearerToken() {
        String[] properties = {
                "sprouts.security.credential-provider.id=333ef2a7-2a43-43f3-9222-b7798a3a2cdc",
                "sprouts.security.credential-provider.credential.type=BearerToken",
                "sprouts.security.credential-provider.credential.bearer-token.header=Authorization",
                "sprouts.security.credential-provider.credential.bearer-token.key.algorithm=HS512",
                "sprouts.security.credential-provider.credential.bearer-token.key.encode-type=BASE64URL",
                "sprouts.security.credential-provider.credential.bearer-token.key.encoded-string=tcVIOpfZ8zLgo5erz5Vy2Ou3U4m97WrInt3LE4K7Gx8_daJNWxqRstS299PZCku4i6UjZA3CeMkg_seMqJuz4g==",
        };

        this.applicationContextRunner.withPropertyValues(properties).run(context -> {
            assertThat(context).hasSingleBean(BearerTokenProvider.class);
            UUID memberId = UUID.randomUUID();
            UUID clientId = UUID.randomUUID();
            Long validityInSeconds = 60L;

            Credential credential = context.getBean(BearerTokenProvider.class).provide(BearerTokenParam.of(memberId, clientId, validityInSeconds));
            log.info(credential.getValue());

            Claims claims = context.getBean(BearerTokenProvider.class).consume(credential.getValue());

            assertThat(UUID.fromString(claims.getIssuer()).equals(context.getBean(CredentialProviderProperty.class).getId())).isTrue();
            assertThat(UUID.fromString(claims.getSubject()).equals(memberId)).isTrue();
            assertThat(UUID.fromString(claims.getAudience()).equals(clientId)).isTrue();
        });
    }
}