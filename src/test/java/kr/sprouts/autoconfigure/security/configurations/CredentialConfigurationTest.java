package kr.sprouts.autoconfigure.security.configurations;

import io.jsonwebtoken.Claims;
import kr.sprouts.autoconfigure.security.components.ApiKeyParam;
import kr.sprouts.autoconfigure.security.components.ApiKeyProvider;
import kr.sprouts.autoconfigure.security.components.BearerTokenParam;
import kr.sprouts.autoconfigure.security.components.BearerTokenProvider;
import kr.sprouts.autoconfigure.security.enumerations.AlgorithmType;
import kr.sprouts.autoconfigure.security.properties.CredentialProperty;
import kr.sprouts.autoconfigure.security.utilities.CredentialUtility;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import java.util.Base64;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class CredentialConfigurationTest {
    private final ApplicationContextRunner applicationContextRunner = new ApplicationContextRunner().withConfiguration(AutoConfigurations.of(
            CredentialConfiguration.class
    ));

    @Test
    void generate_key() {
        log.info(Base64.getUrlEncoder().encodeToString(CredentialUtility.secretKeyFor(AlgorithmType.AES).getEncoded()));
        log.info(Base64.getUrlEncoder().encodeToString(CredentialUtility.keyPairFor(AlgorithmType.ES256).getPrivate().getEncoded()));
    }

    @Test
    void bean_test_apiKey() {
        String[] properties = {
                "sprouts.security.credential.api-key.header=API-KEY",
                "sprouts.security.credential.api-key.key.algorithm=AES",
                "sprouts.security.credential.api-key.key.encode-type=BASE64URL",
                "sprouts.security.credential.api-key.key.encoded-string=5150xqtD36SDH01oeBGQmcZifuvS0W_RncdGDAhMui4=",
                "sprouts.security.credential.bearer-token.header=Authorization",
                "sprouts.security.credential.bearer-token.key.algorithm=HS512",
                "sprouts.security.credential.bearer-token.key.encode-type=BASE64URL",
                "sprouts.security.credential.bearer-token.key.encoded-string=tcVIOpfZ8zLgo5erz5Vy2Ou3U4m97WrInt3LE4K7Gx8_daJNWxqRstS299PZCku4i6UjZA3CeMkg_seMqJuz4g==",
        };

        this.applicationContextRunner.withPropertyValues(properties).run(context -> {
            assertThat(context).hasSingleBean(CredentialConfiguration.class);
            assertThat(context).hasSingleBean(CredentialProperty.class);
            assertThat(context).hasSingleBean(ApiKeyProvider.class);

            UUID providerId = UUID.randomUUID();
            UUID memberId = UUID.randomUUID();
            UUID clientId = UUID.randomUUID();

            assertThat(context.getBean(ApiKeyProvider.class).consume(context.getBean(ApiKeyProvider.class).provide(ApiKeyParam.of(providerId, memberId, clientId)).getValue()).equals(String.format("%s/%s/%s", providerId, memberId, clientId))).isTrue();
        });
    }

    @Test
    void bean_test_bearerToken() {
        String[] properties = {
                "sprouts.security.credential.api-key.header=API-KEY",
                "sprouts.security.credential.api-key.key.algorithm=AES",
                "sprouts.security.credential.api-key.key.encode-type=BASE64URL",
                "sprouts.security.credential.api-key.key.encoded-string=5150xqtD36SDH01oeBGQmcZifuvS0W_RncdGDAhMui4=",
                "sprouts.security.credential.bearer-token.header=Authorization",
                "sprouts.security.credential.bearer-token.key.algorithm=HS512",
                "sprouts.security.credential.bearer-token.key.encode-type=BASE64URL",
                "sprouts.security.credential.bearer-token.key.encoded-string=tcVIOpfZ8zLgo5erz5Vy2Ou3U4m97WrInt3LE4K7Gx8_daJNWxqRstS299PZCku4i6UjZA3CeMkg_seMqJuz4g==",
        };

        this.applicationContextRunner.withPropertyValues(properties).run(context -> {
            assertThat(context).hasSingleBean(BearerTokenProvider.class);

            UUID providerId = UUID.randomUUID();
            UUID memberId = UUID.randomUUID();
            UUID clientId = UUID.randomUUID();
            Long validityInSeconds = 60L;

            Claims claims = context.getBean(BearerTokenProvider.class).consume(context.getBean(BearerTokenProvider.class).provide(BearerTokenParam.of(providerId, memberId, clientId, validityInSeconds)).getValue());

            assertThat(UUID.fromString(claims.getIssuer()).equals(providerId)).isTrue();
            assertThat(UUID.fromString(claims.getSubject()).equals(memberId)).isTrue();
            assertThat(UUID.fromString(claims.getAudience()).equals(clientId)).isTrue();
        });
    }
}