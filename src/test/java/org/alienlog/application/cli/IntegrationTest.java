package org.alienlog.application.cli;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class IntegrationTest {

    private static final String SIGHTING_JSON = "{" +
            "  \"latitude\" : 10.2," +
            "  \"longitude\" : 4.8," +
            "  \"createdAt\" : \"2022-09-21T00:00:00Z\"," +
            "  \"type\" : \"ABDUCTION\"" +
            "}";
    private WireMockServer wireMockServer;
    private ArgParser argParser;
    private AlienLogAPIClient apiClient;
    private Application application;
    private String[] args;

    @BeforeEach
    void setUp() {
        argParser = new ArgParser();
        apiClient = new AlienLogAPIClient();

        wireMockServer = new WireMockServer(wireMockConfig().port(8089));
        wireMockServer.start();
        wireMockServer.stubFor(post("/api/sightings")
                .willReturn(ok()
                        .withHeader("Content-Type", "text/xml")
                        .withBody("<response>SUCCESS</response>")));

        application = new Application(argParser, apiClient);
    }

    @AfterEach
    void tearDown() {
        wireMockServer.stop();
    }


    @Test
    void testSuccessfulSubmit() {
        application.run(new String[]{"10.2", "4.8", "2022-09-21T00:00:00Z", "abduction"});
        wireMockServer.verify(postRequestedFor(urlEqualTo("/api/sightings"))
                .withRequestBody(equalToJson(SIGHTING_JSON)));
    }

    @Test
    void testSubmissionWithInvalidCoordinates() {
        args = new String[]{"10.2°", "4.8°", "2022-09-21T00:00:00Z", "abduction"};

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> application.run(args));
        assertEquals(exception.getMessage(),
                "Invalid coordinate format: ");

        wireMockServer.verify(exactly(0), postRequestedFor(urlEqualTo("/api/sightings")));

    }

    @Test
    void testSubmissionWithInvalidDate() {
        args = new String[]{"10.2", "4.8", "2022-09-21", "abduction"};

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> application.run(args));
        assertEquals(exception.getMessage(),
                "Invalid date format: ");

        wireMockServer.verify(exactly(0), postRequestedFor(urlEqualTo("/api/sightings")));

    }

    @Test
    void testFailedSubmitWithInvalidType() {
        args = new String[]{"10.2", "4.8", "2022-09-21T00:00:00Z", "i don't know"};

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> application.run(args));
        assertEquals(exception.getMessage(),
                "Invalid sighting type: ");

        wireMockServer.verify(exactly(0), postRequestedFor(urlEqualTo("/api/sightings")));

    }

    @Test
    void testSubmissionWithIncorrectArgumentsCount() {
        args = new String[]{"10.2", "4.8", "2022-09-21T00:00:00Z"};

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> application.run(args));
        assertEquals(exception.getMessage(),
                "Invalid number of arguments");

        wireMockServer.verify(exactly(0), postRequestedFor(urlEqualTo("/api/sightings")));

    }

    @Test
    void testSubmissionWithEmptyArguments() {
        args = new String[]{};

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> application.run(args));
        assertEquals(exception.getMessage(),
                "Invalid number of arguments");

        wireMockServer.verify(exactly(0), postRequestedFor(urlEqualTo("/api/sightings")));

    }
}
