package com.bt2.spotify_consumer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
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

    private final AuthService authService;
    private final RestTemplate restTemplate;
    private final String SPOTIFY_API_ARTISTS_URL = "https://api.spotify.com/v1/me/top/artists";
    private final String SPOTIFY_API_SINGLE_ARTIST_URL = "https://api.spotify.com/v1/artists/";
    private final String SPOTIFY_API_SINGLE_ALBUM_URL = "https://api.spotify.com/v1/albums/";

    public SpotifyService(RestTemplate restTemplate, AuthService authService) {
        this.restTemplate = restTemplate;
        this.authService = authService;
    }


    public Map<String, Object> fetchArtists(String accessToken, String refreshToken) {
        try {
            HttpEntity<String> entity = createHttpEntity(accessToken);
            ResponseEntity<String> response = restTemplate.exchange(SPOTIFY_API_ARTISTS_URL, HttpMethod.GET, entity, String.class);
            return parseResponseToMap(response.getBody());
        } catch (HttpClientErrorException.Unauthorized ex) { // Retry and refresh
                return fetchArtistsWithNewTokens(refreshToken);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private Map<String, Object> fetchArtistsWithNewTokens(String refreshToken) {
        // Refresh tokens
        Map<String, String> updatedTokens = authService.refreshAccessToken(refreshToken);
        String newAccessToken = updatedTokens.get("access_token");
        String newRefreshToken = updatedTokens.get("refresh_token");
        HttpEntity<String> newEntity = createHttpEntity(newAccessToken);
        ResponseEntity<String> response = restTemplate.exchange(SPOTIFY_API_ARTISTS_URL, HttpMethod.GET, newEntity, String.class);

        // Parse and add tokens
        Map<String, Object> responseData = null;
        try {
            responseData = this.parseResponseToMap(response.getBody());
            return addTokensToResponse((HttpEntity) responseData, newAccessToken, newRefreshToken);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private Map<String, Object> parseResponseToMap(String responseBody) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(responseBody, Map.class);
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

            Map<String, Object> artist = objectMapper.readValue(artistJson, Map.class);
            Map<String, Object> albums = objectMapper.readValue(albumsJson, Map.class);
            Map<String, Object> topTracks = objectMapper.readValue(topTracksJson, Map.class);

            artistInfo.put("artist", artist);
            artistInfo.put("albums", albums);
            artistInfo.put("top_tracks", topTracks);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return artistInfo;
    }

    private String fetchArtistInfo(String accessToken, String refreshToken, String id, String infoType) {
        HttpEntity<String> entity = createHttpEntity(accessToken);
        // If infoType is null or empty, only get the artist; else add infoType
        String url = SPOTIFY_API_SINGLE_ARTIST_URL + id + (infoType != null && !infoType.isEmpty() ? "/" + infoType : "");
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        return response.getBody();
    }

    @Override
    public String fetchAlbumInfo(String accessToken, String refreshToken, String id) {
        HttpEntity<String> entity = createHttpEntity(accessToken);
        String url = SPOTIFY_API_SINGLE_ALBUM_URL + id;
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        return response.getBody();

    }

    @Override
    public String fetchQuery(String accessToken, String refreshToken, String type, String query) {
        HttpEntity<String> entity = createHttpEntity(accessToken);
        String url = "https://api.spotify.com/v1/search?q=" + query + "&type=" + type + "&limit=5";

        try {
            return restTemplate.exchange(url, HttpMethod.GET, entity, String.class).getBody();
        } catch (Exception ex) {
            throw new RuntimeException("Unexpected error occurred: " + ex.getMessage(), ex);
        }
    }

    private HttpEntity<String> createHttpEntity(String accessToken){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", accessToken);
        return new HttpEntity<String>(headers);
    }

    private Map<String, Object> addTokensToResponse(HttpEntity response, String newAccessToken, String newRefreshToken) {
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("data", response.getBody());
        responseData.put("access_token", newAccessToken);
        responseData.put("refresh_token", newRefreshToken);
        return responseData;
    }
}
