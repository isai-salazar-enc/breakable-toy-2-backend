package com.bt2.spotify_consumer.utils;

import com.bt2.spotify_consumer.exception.UnauthorizedException;

public class SpotifyValidations {

    public static void validateBearerToken(String accessToken) {
        if (accessToken == null || !accessToken.startsWith("Bearer ")) {
            throw new UnauthorizedException("Invalid or missing Bearer token");
        }
    }
}
