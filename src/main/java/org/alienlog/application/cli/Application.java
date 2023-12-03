package org.alienlog.application.cli;

import com.github.tomakehurst.wiremock.WireMockServer;

import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

public class Application {

    private final ArgParser argParser;
    private final AlienLogAPIClient apiClient;

    public Application(ArgParser argParser, AlienLogAPIClient apiClient) {
        this.argParser = argParser;
        this.apiClient = apiClient;
    }

    public void run(String[] args) {
        // Ova metoda je zasebna kako bi ju se decoupliralo od main metode. Naime, nije nužno potreban mock server u
        // svakom testu. Zahvaljujući konstruktoru, možemo injektirati mock verzije klasa ArgParser i AlienLogAPIClient.
        apiClient.submit(argParser.parse(args));
        System.out.println("Your sighting has been submitted!");
    }

    public static void main(String[] args) {
        // https://wiremock.org/docs/multi-domain-mocking/
        WireMockServer wireMockServer = new WireMockServer(wireMockConfig().port(8089));
        wireMockServer.start();
        wireMockServer.stubFor(post("/api/sightings")
                        .willReturn(ok()
                        .withHeader("Content-Type", "text/xml")
                        .withBody("<response>SUCCESS</response>")));
        Application application = new Application(new ArgParser(), new AlienLogAPIClient());
        application.run(args);
        wireMockServer.stop();
    }
}