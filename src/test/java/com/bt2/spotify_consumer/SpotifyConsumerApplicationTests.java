package com.bt2.spotify_consumer;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
		"spotify.redirect.uri=${SPOTIFY_REDIRECT_URI}"
})
class SpotifyConsumerApplicationTests {

	@Test
	void contextLoads() {
	}

}
