package com.bt2.spotify_consumer.controller;

import com.bt2.spotify_consumer.service.AuthServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Map;

@RestController
public class AuthController {

    @Autowired
    private AuthServiceInterface authService;

    // Receive the code and return the authorization token
    public ResponseEntity<?> getSpotifyToken(@RequestBody Map<String, String> body){
        String code = body.get("code");
        if (code == null || code.isEmpty()) {
            return ResponseEntity.badRequest().body("Error: 'code' is required");
        }

        try{
            return ResponseEntity.ok(authService.getSpotifyToken(code));
        } catch (HttpClientErrorException e ){
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }
}
