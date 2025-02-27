package com.bt2.spotify_consumer.service;

import java.util.Map;

public interface AuthServiceInterface {
    public Map<String, Object> getSpotifyToken(String code);
}
