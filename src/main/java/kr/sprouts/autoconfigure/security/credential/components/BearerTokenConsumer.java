package kr.sprouts.autoconfigure.security.credential.components;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

public class BearerTokenConsumer extends CredentialConsumer {
    private BearerTokenConsumer(Algorithm algorithm, Encoding encoding, String encodedSecretKey) {
        super(algorithm, encoding, encodedSecretKey);
    }

    public static BearerTokenConsumer of(Algorithm algorithm, Encoding encoding, String encodedSecretKey) {
        return new BearerTokenConsumer(algorithm, encoding, encodedSecretKey);
    }

    @Override
    public Principal consume(String claimsJws) {
        return Principal.fromClaims(this.parse(claimsJws));
    }

    private Claims parse(String claimsJws) {
        return Jwts.parserBuilder()
                .setSigningKey(this.getSecretKey())
                .build()
                .parseClaimsJws(claimsJws)
                .getBody();
    }
}
