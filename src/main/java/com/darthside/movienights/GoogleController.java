package com.darthside.movienights;
import com.darthside.movienights.database.Token;
import com.darthside.movienights.database.TokenTable;
import com.google.api.client.googleapis.auth.oauth2.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;

@RestController
public class GoogleController {

    private static final String CLIENT_ID = "294307018578-mlelfvhktca0k84t1brnho8ssn25dsqe.apps.googleusercontent.com";
    private static final String CLIENT_SECRET = "s4GH7hgV-jKJ2LsUVHuzFYZA";

    @Autowired
    TokenTable tokenTable;

    public static GoogleCredential getRefreshedCredentials(String refreshCode) {
        try {
            GoogleTokenResponse response = new GoogleRefreshTokenRequest(
                    new NetHttpTransport(), JacksonFactory.getDefaultInstance(), refreshCode, CLIENT_ID, CLIENT_SECRET )
                    .execute();

            return new GoogleCredential().setAccessToken(response.getAccessToken());
        }
        catch( Exception ex ){
            ex.printStackTrace();
            return null;
        }
    }


    @RequestMapping(value = "/storeauthcode", method = RequestMethod.POST)
    public String storeauthcode(@RequestBody String code, @RequestHeader("X-Requested-With") String encoding) {

        if (encoding == null || encoding.isEmpty()) {
            // Without the `X-Requested-With` header, this request could be forged. Aborts.
            return "Error, wrong headers";
        }

        GoogleTokenResponse tokenResponse = null;
        try {
            tokenResponse = new GoogleAuthorizationCodeTokenRequest(
                    new NetHttpTransport(),
                    JacksonFactory.getDefaultInstance(),
                    "https://www.googleapis.com/oauth2/v4/token",
                    CLIENT_ID,
                    CLIENT_SECRET,
                    code,
                    "http://localhost:8080")
                    .execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Store these 3in your database
        String accessToken = tokenResponse.getAccessToken();
        String refreshToken = tokenResponse.getRefreshToken();
        long expiresAt = System.currentTimeMillis() + (tokenResponse.getExpiresInSeconds() * 1000);

        // Get profile info from ID token (Obtained at the last step of OAuth2)
        GoogleIdToken idToken = null;
        try {
            idToken = tokenResponse.parseIdToken();
        } catch (IOException e) {
            e.printStackTrace();
        }
        GoogleIdToken.Payload payload = idToken.getPayload();

        // Use THIS ID as a key to identify a google user-account.
        String userId = payload.getSubject();
        String email = payload.getEmail();
//        boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
//        String name = (String) payload.get("name");
//        String pictureUrl = (String) payload.get("picture");
//        String locale = (String) payload.get("locale");
//        String familyName = (String) payload.get("family_name");
//        String givenName = (String) payload.get("given_name");

        // Debugging purposes, should probably be stored in the database instead (At least "givenName").
//        System.out.println("userId: " + userId);
//        System.out.println("email: " + email);
//        System.out.println("emailVerified: " + emailVerified);
//        System.out.println("name: " + name);
//        System.out.println("pictureUrl: " + pictureUrl);
//        System.out.println("locale: " + locale);
//        System.out.println("familyName: " + familyName);
//        System.out.println("givenName: " + givenName);

        // Debug purpose only
        System.out.println("userId: " + userId);
        System.out.println("email: " + email);
        System.out.println("accessToken: " + accessToken);
        System.out.println("refreshToken: " + refreshToken);
        System.out.println("expiresAt: " + expiresAt);

/*        Token token = new Token(email, accessToken, refreshToken, expiresAt);
        tokenTable.save(token);*/
        Token token = new Token();
        token.setEmail(email);
        token.setAccessToken(accessToken);
        token.setRefreshToken(refreshToken);
        token.setExpiresAt(expiresAt);
        tokenTable.save(token);


        return "OK";
    }


}
