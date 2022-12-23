package kr.sprouts.autoconfigure.security.components;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@RequiredArgsConstructor(access = AccessLevel.PUBLIC, staticName = "of")
public class ApiKeyParam extends CredentialParam {
    @NonNull
    private UUID memberId;
    @NonNull
    private UUID clientId;
}
