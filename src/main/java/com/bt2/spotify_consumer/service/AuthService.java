package com.bt2.spotify_consumer.service;

import com.bt2.spotify_consumer.config.SpotifyClientConfiguration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

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
}
