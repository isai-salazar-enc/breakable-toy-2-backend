package com.bt2.spotify_consumer.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@RequestMapping("/auth")
public interface AuthControllerInterface {

    // Receive the code and return the authorization token
    @PostMapping("/spotify")
    public ResponseEntity<?> getSpotifyToken(@RequestBody Map<String, String> body);
}
