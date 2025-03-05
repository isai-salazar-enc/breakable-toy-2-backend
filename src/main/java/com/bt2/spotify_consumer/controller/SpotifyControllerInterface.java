package com.bt2.spotify_consumer.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

public interface SpotifyControllerInterface {

    @GetMapping("/me/top/artists")
    public ResponseEntity<?> getTopArtists(@RequestHeader("Authorization") String accessToken, @RequestHeader("Refresh-Token") String refreshToken);

// TODO:
//    @GetMapping("/artists/{id}")
//    public ResponseEntity<?> getArtistInfo(@PathVariable(value = "id") Long id);
// TODO:
//    @GetMapping("/albums/{id}")
//    public ResponseEntity<?> getAlbumTracks(@PathVariable(value = "id") Long id);
}