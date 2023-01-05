package kr.sprouts.autoconfigure.security.credential;

import kr.sprouts.autoconfigure.security.credential.components.ApiKeyConsumer;
import kr.sprouts.autoconfigure.security.credential.components.ApiKeyParam;
import kr.sprouts.autoconfigure.security.credential.components.ApiKeyProvider;
import kr.sprouts.autoconfigure.security.credential.components.BearerTokenConsumer;
import kr.sprouts.autoconfigure.security.credential.components.BearerTokenParam;
import kr.sprouts.autoconfigure.security.credential.components.BearerTokenProvider;
import kr.sprouts.autoconfigure.security.credential.components.Credential;
import kr.sprouts.autoconfigure.security.credential.components.Principal;
import kr.sprouts.autoconfigure.security.credential.configurations.CredentialConfiguration;
import kr.sprouts.autoconfigure.security.credential.properties.CredentialProperty;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class MockApplicationTest {
    private final ApplicationContextRunner applicationContextRunner = new ApplicationContextRunner().withConfiguration(AutoConfigurations.of(
            CredentialConfiguration.class
    ));

    @Test
    void api_key() {
        String[] properties = {
                "sprouts.security.credential.api-key.header=X-API-KEY",
                "sprouts.security.credential.api-key.key.algorithm=AES",
                "sprouts.security.credential.api-key.key.encoding=Base64Url",
                "sprouts.security.credential.api-key.key.encoded-secret-key=5150xqtD36SDH01oeBGQmcZifuvS0W_RncdGDAhMui4=",
                "sprouts.security.credential.bearer-token.header=Authorization",
                "sprouts.security.credential.bearer-token.key.algorithm=HmacSHA512",
                "sprouts.security.credential.bearer-token.key.encoding=Base64Url",
                "sprouts.security.credential.bearer-token.key.encoded-secret-key=tcVIOpfZ8zLgo5erz5Vy2Ou3U4m97WrInt3LE4K7Gx8_daJNWxqRstS299PZCku4i6UjZA3CeMkg_seMqJuz4g==",
        };

        this.applicationContextRunner.withPropertyValues(properties).run(context -> {
            assertThat(context).hasSingleBean(CredentialConfiguration.class);
            assertThat(context).hasSingleBean(CredentialProperty.class);
            assertThat(context).hasSingleBean(ApiKeyProvider.class);
            assertThat(context).hasSingleBean(ApiKeyConsumer.class);

            UUID providerId = UUID.randomUUID();
            UUID consumerId = UUID.randomUUID();
            UUID memberId = UUID.randomUUID();

            Credential credential = context.getBean(ApiKeyProvider.class).provide(ApiKeyParam.of(providerId, consumerId, memberId));
            Principal principal = context.getBean(ApiKeyConsumer.class).consume(credential.getValue());

            assertThat(principal.getProviderId()).isEqualTo(providerId);
            assertThat(principal.getConsumerId()).isEqualTo(consumerId);
            assertThat(principal.getMemberId()).isEqualTo(memberId);
        });
    }

    @Test
    void bearer_token() {
        String[] properties = {
                "sprouts.security.credential.api-key.header=X-API-KEY",
                "sprouts.security.credential.api-key.key.algorithm=AES",
                "sprouts.security.credential.api-key.key.encoding=Base64Url",
                "sprouts.security.credential.api-key.key.encoded-secret-key=5150xqtD36SDH01oeBGQmcZifuvS0W_RncdGDAhMui4=",
                "sprouts.security.credential.bearer-token.header=Authorization",
                "sprouts.security.credential.bearer-token.key.algorithm=HmacSHA512",
                "sprouts.security.credential.bearer-token.key.encoding=Base64Url",
                "sprouts.security.credential.bearer-token.key.encoded-secret-key=tcVIOpfZ8zLgo5erz5Vy2Ou3U4m97WrInt3LE4K7Gx8_daJNWxqRstS299PZCku4i6UjZA3CeMkg_seMqJuz4g==",
        };

        this.applicationContextRunner.withPropertyValues(properties).run(context -> {
            assertThat(context).hasSingleBean(CredentialConfiguration.class);
            assertThat(context).hasSingleBean(CredentialProperty.class);
            assertThat(context).hasSingleBean(BearerTokenProvider.class);
            assertThat(context).hasSingleBean(BearerTokenConsumer.class);

            UUID providerId = UUID.randomUUID();
            UUID consumerId = UUID.randomUUID();
            UUID memberId = UUID.randomUUID();

            Credential credential = context.getBean(BearerTokenProvider.class).provide(BearerTokenParam.of(providerId, consumerId, memberId, 60L));
            Principal principal = context.getBean(BearerTokenConsumer.class).consume(credential.getValue());

            assertThat(principal.getProviderId()).isEqualTo(providerId);
            assertThat(principal.getConsumerId()).isEqualTo(consumerId);
            assertThat(principal.getMemberId()).isEqualTo(memberId);
        });
    }
}
