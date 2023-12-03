package org.alienlog.application.cli;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.mockito.Mockito.*;


class ApplicationTest {

    private ArgParser mockArgParser;
    private AlienLogAPIClient mockApiClient;
    private Application application;

    @BeforeEach
    void setUp() {
        mockArgParser = mock(ArgParser.class);
        mockApiClient = mock(AlienLogAPIClient.class);
        application = new Application(mockArgParser, mockApiClient);
    }

    @Test
    void testApplicationRuns() {
        UFOSighting ufoSighting = new UFOSighting(10.2, 4.8,
                Instant.parse("2022-09-21T00:00:00Z"), UFOSightingType.ABDUCTION);
        doReturn(ufoSighting).when(mockArgParser).parse(any());
        application.run(new String[]{"10.2", "4.8", "2022-09-21T00:00:00Z", "abduction"});
        verify(mockApiClient).submit(ufoSighting);
    }
}