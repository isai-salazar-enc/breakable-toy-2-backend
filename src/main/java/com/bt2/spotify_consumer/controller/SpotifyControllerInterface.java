package com.bt2.spotify_consumer.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

public interface SpotifyControllerInterface {

    @GetMapping("/me/top/artists")
    public ResponseEntity<?> getTopArtists(@RequestHeader("Authorization") String accessToken, @RequestHeader("Refresh-Token") String refreshToken);

    @GetMapping("/artists/{id}")
    public ResponseEntity<?> getArtistInfo(@RequestHeader("Authorization") String accessToken, @RequestHeader("Refresh-Token") String refreshToken, @PathVariable(value = "id") String id);

// TODO:
//    @GetMapping("/albums/{id}")
//    public ResponseEntity<?> getAlbumTracks(@PathVariable(value = "id") Long id);
}