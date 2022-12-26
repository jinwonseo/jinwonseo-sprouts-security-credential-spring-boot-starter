package kr.sprouts.autoconfigure.security.components;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import kr.sprouts.autoconfigure.security.enumerations.AlgorithmType;
import kr.sprouts.autoconfigure.security.enumerations.CredentialType;
import kr.sprouts.autoconfigure.security.enumerations.EncodeType;
import lombok.Getter;

import javax.crypto.SecretKey;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

@Getter
public class BearerTokenProvider extends CredentialProvider {
    private final String header;
    private final AlgorithmType algorithmType;
    private final EncodeType encodeType;
    private final SecretKey secretKey;

    private BearerTokenProvider(String header, AlgorithmType algorithmType, EncodeType encodeType, String encodedSecretKeyString) {
        if (!Arrays.asList(algorithmType.getCredentialTypes()).contains(CredentialType.BEARER_TOKEN)) {
            throw new IllegalArgumentException("Not available algorithm");
        }

        this.header = header;
        this.algorithmType = algorithmType;
        this.encodeType = encodeType;

        SecretKey secretKey = this.convertToSecretKey(encodedSecretKeyString);

        if (algorithmType.getMinKeyLength() > secretKey.getEncoded().length * 8) {
            throw new IllegalArgumentException("Not available secret key size");
        }

        this.secretKey = secretKey;
    }

    public static BearerTokenProvider of(String header, AlgorithmType algorithmType, EncodeType encodeType, String encodedSecretKeyString) {
        return new BearerTokenProvider(header, algorithmType, encodeType, encodedSecretKeyString);
    }

    @Override
    public BearerToken provide(CredentialParam param) {
        if (!(param instanceof BearerTokenParam)) {
            throw new IllegalArgumentException("Unexpected parameter " + param.getClass().getSimpleName());
        }

        return BearerToken.of(header, this.create(
                ((BearerTokenParam)param).getProviderId(),
                ((BearerTokenParam)param).getMemberId(),
                ((BearerTokenParam)param).getClientId(),
                ((BearerTokenParam)param).getValidityInSeconds())
        );
    }

    public Claims consume(String claimsJws) {
        return this.parse(claimsJws);
    }

    private String create(UUID providerId, UUID memberId, UUID clientId, Long validityInSeconds) {
        LocalDateTime currentLocalDateTime = LocalDateTime.now();

        Claims claims = Jwts.claims();

        claims.setId(UUID.randomUUID().toString());
        claims.setIssuer(providerId.toString());
        claims.setSubject(memberId.toString());
        claims.setAudience(clientId.toString());
        claims.setNotBefore(Timestamp.valueOf(currentLocalDateTime));
        claims.setIssuedAt(Timestamp.valueOf(currentLocalDateTime));
        claims.setExpiration(Timestamp.valueOf(currentLocalDateTime.plusSeconds(validityInSeconds)));

        return Jwts.builder()
                .setClaims(claims)
                .signWith(this.secretKey, this.algorithmType.getSignatureAlgorithm())
                .compact();
    }

    private Claims parse(String claimsJws) {
        return Jwts.parserBuilder()
                .setSigningKey(this.secretKey)
                .build()
                .parseClaimsJws(claimsJws)
                .getBody();
    }

    private String encode(byte[] encryptedValue) {
        return this.encodeType.getEncoder().encodeToString(encryptedValue);
    }

    private byte[] decode(String encodedValue) {
        return this.encodeType.getDecoder().decode(encodedValue);
    }

    private byte[] decode(byte[] encodedByte) {
        return this.encodeType.getDecoder().decode(encodedByte);
    }

    private SecretKey convertToSecretKey(String encodedSecretKeyString) {
        return Keys.hmacShaKeyFor(this.decode(encodedSecretKeyString.getBytes()));
    }
}
