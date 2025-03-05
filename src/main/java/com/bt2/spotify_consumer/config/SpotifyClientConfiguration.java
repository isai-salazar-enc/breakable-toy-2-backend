package com.bt2.spotify_consumer.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@Getter
public class SpotifyClientConfiguration {

    // Manage RestTemplate as a bean to inject it and avoid creating new instances in each method
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Value("${spotify.client.id}")
    private String clientId;

    @Value("${spotify.client.secret}")
    private String clientSecret;

    @Value("${spotify.redirect.uri}")
    private String redirectUri;

    private final String spotifyTokenUrl = "https://accounts.spotify.com/api/token";

}

