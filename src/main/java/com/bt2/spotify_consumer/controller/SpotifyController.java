package com.bt2.spotify_consumer.controller;

import com.bt2.spotify_consumer.service.SpotifyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class SpotifyController implements SpotifyControllerInterface{
    private final SpotifyService spotifyService;

    public SpotifyController(SpotifyService spotifyService){
        this.spotifyService = spotifyService;
    }

    @Override
    public ResponseEntity<?> getTopArtists(String accessToken, String refreshToken) {
        if (accessToken == null || !accessToken.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or missing Bearer token");
        }

        String response = spotifyService.fetchArtists(accessToken, refreshToken);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<?> getArtistInfo(String accessToken, String refreshToken, String id) {
        if (accessToken == null || !accessToken.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or missing Bearer token");
        }

        Map<String, Object> response = spotifyService.fetchSingleArtist(accessToken, refreshToken, id);
        return ResponseEntity.ok(response);
    }


}
