package com.bt2.spotify_consumer.service;

import java.util.Map;

public interface SpotifyServiceInterface {
    public String fetchArtists(String accessToken, String refreshToken);
    public Map<String, Object> fetchSingleArtist (String accessToken, String refreshToken, String id);
    public String fetchAlbumInfo (String accessToken, String refreshToken, String id);
}
