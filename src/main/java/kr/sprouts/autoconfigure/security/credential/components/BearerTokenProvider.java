package kr.sprouts.autoconfigure.security.credential.components;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

public class BearerTokenProvider extends CredentialProvider {

    private BearerTokenProvider(String header, Algorithm algorithm, Encoding encoding, String encodedSecretKey) {
        super(header, algorithm, encoding, encodedSecretKey);
    }

    public static BearerTokenProvider of(String header, Algorithm algorithm, Encoding encoding, String encodedSecretKey) {
        return new BearerTokenProvider(header, algorithm, encoding, encodedSecretKey);
    }

    @Override
    public Credential provide(CredentialParam param) {
        if (!(param instanceof BearerTokenParam)) {
            throw new IllegalArgumentException("Unexpected parameter " + param.getClass().getSimpleName());
        }

        return Credential.of(
                Credential.Type.BEARER_TOKEN,
                this.getHeader(),
                this.create(Principal.fromCredentialParam(param), ((BearerTokenParam) param).getValidityInSeconds())
        );
    }

    private String create(Principal principal, Long validityInSeconds) {
        LocalDateTime currentLocalDateTime = LocalDateTime.now();

        Claims claims = Jwts.claims();

        claims.setId(UUID.randomUUID().toString());
        claims.setIssuer(principal.getProviderId().toString());
        claims.setSubject(principal.getMemberId().toString());
        claims.setAudience(principal.getConsumerId().toString());
        claims.setNotBefore(Timestamp.valueOf(currentLocalDateTime));
        claims.setIssuedAt(Timestamp.valueOf(currentLocalDateTime));
        claims.setExpiration(Timestamp.valueOf(currentLocalDateTime.plusSeconds(validityInSeconds)));

        return Jwts.builder()
                .setClaims(claims)
                .signWith(this.getSecretKey(), SignatureAlgorithm.forName(this.getAlgorithm().getSimpleName()))
                .compact();
    }
}
