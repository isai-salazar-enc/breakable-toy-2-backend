package com.bt2.spotify_consumer.service;

import com.bt2.spotify_consumer.config.SpotifyClientConfiguration;
import com.bt2.spotify_consumer.exception.UnauthorizedException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.print.attribute.standard.Media;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService implements AuthServiceInterface {

    private final SpotifyClientConfiguration spotifyConfig;

    public AuthService(SpotifyClientConfiguration spotifyConfig) {
        this.spotifyConfig = spotifyConfig;
    }

    @Override
    public Map<String, Object> getSpotifyToken(String code) {
        // Create http headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED); // Required type by ouath
        headers.setBasicAuth(spotifyConfig.getClientId(), spotifyConfig.getClientSecret()); // Oauth requests basic auth on headers

        // Create http body
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("code", code);
        params.add("redirect_uri", spotifyConfig.getRedirectUri());

        // Create http request
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response = restTemplate.postForEntity(spotifyConfig.getSpotifyTokenUrl(), request, Map.class); //url, request, response type

        return response.getBody();
    }

    public Map<String, String> refreshAccessToken(String refreshToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth(spotifyConfig.getClientId(), spotifyConfig.getClientSecret()); // Oauth requests basic auth on headers

        // Create http body
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "refresh_token");
        params.add("refresh_token", refreshToken);

        // Create http request
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> responseBody = restTemplate.postForEntity(spotifyConfig.getSpotifyTokenUrl(), request, Map.class).getBody(); //url, request, response type
        if (responseBody != null && responseBody.containsKey("refresh_token") && responseBody.containsKey("access_token")) {
            Map<String, String> tokens = new HashMap<>();
            tokens.put("access_token", responseBody.get("access_token"));
            tokens.put("refresh_token", responseBody.get("refresh_token"));
            return tokens;
        }

        throw new UnauthorizedException("Invalid token");
    }
}
