package com.bt2.spotify_consumer.service;

public interface SpotifyServiceInterface {
    public String fetchArtists(String accessToken, String refreshToken);
}
