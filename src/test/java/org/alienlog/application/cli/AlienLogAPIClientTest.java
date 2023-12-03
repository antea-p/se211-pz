package org.alienlog.application.cli;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

class AlienLogAPIClientTest {

    private static final String SIGHTING_JSON = "{" +
            "  \"latitude\" : 10.2," +
            "  \"longitude\" : 4.8," +
            "  \"createdAt\" : \"2022-09-21T00:00:00Z\"," +
            "  \"type\" : \"ABDUCTION\"" +
            "}";
    private WireMockServer wireMockServer;
    private AlienLogAPIClient apiClient;

    @BeforeEach
    void setUp() {
        apiClient = new AlienLogAPIClient();
        wireMockServer = new WireMockServer(wireMockConfig().port(8089));
        wireMockServer.start();
        wireMockServer.stubFor(post("/api/sightings")
                .willReturn(ok()
                        .withHeader("Content-Type", "text/xml")
                        .withBody("<response>SUCCESS</response>")));
    }

    @AfterEach
    void tearDown() {
        wireMockServer.stop();
    }

    @Test
    void testSubmit() {
        UFOSighting ufoSighting = new UFOSighting(10.2, 4.8,
                Instant.parse("2022-09-21T00:00:00Z"), UFOSightingType.ABDUCTION);
        apiClient.submit(ufoSighting);
        wireMockServer.verify(postRequestedFor(urlEqualTo("/api/sightings"))
                .withRequestBody(equalToJson(SIGHTING_JSON)));
    }

}