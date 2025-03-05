package com.bt2.spotify_consumer.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import java.util.Map;
import java.util.HashMap;

@Service
public class SpotifyService implements SpotifyServiceInterface{

    private final RestTemplate restTemplate;
    private final String SPOTIFY_API_ARTISTS_URL = "https://api.spotify.com/v1/me/top/artists";
    private final String SPOTIFY_API_SINGLE_ARTIST_URL = "https://api.spotify.com/v1/artists/";
    private final String SPOTIFY_API_SINGLE_ALBUM_URL = "https://api.spotify.com/v1/albums/";

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

    // TODO: Manage automatic token refreshing
    // Artist name, image, about, popular songs and related artists
    @Override
    public Map<String, Object> fetchSingleArtist(String accessToken, String refreshToken, String id){
        Map<String, Object> artistInfo = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper(); // Jackson Object Mapper to wrap response in correct json format

        try {
            String artistJson = fetchArtistInfo(accessToken, refreshToken, id, "");
            String albumsJson = fetchArtistInfo(accessToken, refreshToken, id, "albums?limit=5");
            String topTracksJson = fetchArtistInfo(accessToken, refreshToken, id, "top-tracks");
//            String related_artists = fetchArtistInfo(accessToken, refreshToken, id, "related-artists");

            Map<String, Object> artist = objectMapper.readValue(artistJson, Map.class);
            Map<String, Object> albums = objectMapper.readValue(albumsJson, Map.class);
            Map<String, Object> topTracks = objectMapper.readValue(topTracksJson, Map.class);

            artistInfo.put("artist", artist);
            artistInfo.put("albums", albums);
            artistInfo.put("top-tracks", topTracks);
//            artistInfo.put("related-artists", related_artists);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return artistInfo;
    }

    private String fetchArtistInfo(String accessToken, String refreshToken, String id, String infoType) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            // If infoType is null or empty, only get the artist; else add infoType
            String url = SPOTIFY_API_SINGLE_ARTIST_URL + id + (infoType != null && !infoType.isEmpty() ? "/" + infoType : "");
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            return response.getBody();
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            throw new RuntimeException("Error fetching artist information from Spotify: " + ex.getMessage(), ex);
        } catch (Exception ex) {
            throw new RuntimeException("Unexpected error occurred: " + ex.getMessage(), ex);
        }
    }

    @Override
    public String fetchAlbumInfo(String accessToken, String refreshToken, String id) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            String url = SPOTIFY_API_SINGLE_ALBUM_URL + id;
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            return response.getBody();
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            throw new RuntimeException("Error fetching artist information from Spotify: " + ex.getMessage(), ex);
        } catch (Exception ex) {
            throw new RuntimeException("Unexpected error occurred: " + ex.getMessage(), ex);
        }
    }

    @Override
    public String fetchQuery(String accessToken, String refreshToken, String type, String query) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        String url = "https://api.spotify.com/v1/search?q=" + query + "&type=" + type + "&limit=5";

        try {
            return restTemplate.exchange(url, HttpMethod.GET, entity, String.class).getBody();
        } catch (Exception ex) {
            throw new RuntimeException("Unexpected error occurred: " + ex.getMessage(), ex);
        }
    }
}
