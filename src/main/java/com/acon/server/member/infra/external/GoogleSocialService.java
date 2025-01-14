package com.acon.server.member.infra.external;

import com.acon.server.global.exception.BusinessException;
import com.acon.server.global.exception.ErrorType;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.json.gson.GsonFactory;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.google.api.client.http.javanet.NetHttpTransport;

@Service
@Slf4j
public class GoogleSocialService {

    @Value("${google.clientId}")
    private String CLIENT_ID;

    private final NetHttpTransport transport = new NetHttpTransport();
    private final GsonFactory jsonFactory = GsonFactory.getDefaultInstance();
    private GoogleIdTokenVerifier verifier;

    @PostConstruct
    void initVerifier() {
        verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                .setAudience(Collections.singletonList(CLIENT_ID))
                .build();
    }

    public String login(String unverifiedIdToken) {
        GoogleIdToken idToken = verifyIdToken(unverifiedIdToken);
        GoogleIdToken.Payload payload = idToken.getPayload();
        return payload.getSubject();
    }

    private GoogleIdToken verifyIdToken(String unverifiedIdToken) {
        try {
            GoogleIdToken idToken = verifier.verify(unverifiedIdToken);
            if (idToken == null) {
                throw new BusinessException(ErrorType.INVALID_ID_TOKEN_ERROR);
            }
            return idToken;
        } catch (GeneralSecurityException | IOException error) {
            throw new BusinessException(ErrorType.FAILED_DOWNLOAD_GOOGLE_PUBLIC_KEY_ERROR);
        }

    }
}
