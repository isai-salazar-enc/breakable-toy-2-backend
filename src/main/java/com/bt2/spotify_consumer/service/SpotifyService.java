package com.bt2.spotify_consumer.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class SpotifyService implements SpotifyServiceInterface{

    private final RestTemplate restTemplate;
    private final String SPOTIFY_API_ARTISTS_URL = "https://api.spotify.com/v1/me/top/artists";

    public SpotifyService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // TODO: Manage automatic token refreshing
    @Override
    public String fetchArtists(String accessToken, String refreshToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        try {
            ResponseEntity<String> response = restTemplate.exchange(SPOTIFY_API_ARTISTS_URL, HttpMethod.GET, entity, String.class);
            return response.getBody();
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            throw new RuntimeException("Error fetching artists from Spotify: " + ex.getMessage(), ex);
        } catch (Exception ex) {
            throw new RuntimeException("Unexpected error occurred: " + ex.getMessage(), ex);
        }
    }
}
