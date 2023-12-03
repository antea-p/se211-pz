package org.alienlog.application.cli;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.http.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

public class AlienLogAPIClient {

    private final RestTemplate restTemplate;

    public AlienLogAPIClient() {
        restTemplate = new RestTemplate();
    }

    /**
     *
     * Predaje UFO sighting na AlienLog API server.
     *
     * @param ufoSighting UFOSighting objekt koji se predaje
     * @throws RestClientException u slučaju greške tijekom komunikacije sa API serverom
     */
    public void submit(UFOSighting ufoSighting) {
        /*
       Podaci su u JSON formatu, te su serijalizirani korištenjem Jackson ObjectMapper klase.
       HTTP zaglavlje zahtjeva uključuje Content-Type: application/json
       */
        String url = "http://localhost:8089/api/sightings";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ObjectMapper mapper = new ObjectMapper()
                .registerModule(new JavaTimeModule()) // registrira modul kompatibilan sa java.time.Instant
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false); // konfig. vremenskog formata
        String jsonBody;
        try {
            jsonBody = mapper.writeValueAsString(ufoSighting);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Failed to serialize sighting to JSON", e);
        }

        HttpEntity<String> requestEntity = new HttpEntity<>(jsonBody, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(url,
                HttpMethod.POST, requestEntity, String.class);
        HttpStatus responseStatus = responseEntity.getStatusCode();
        if (responseStatus.is2xxSuccessful()) {
            System.out.println("Sighting submitted successfully");
        } else {
            throw new RuntimeException("Failed to submit sighting: " + responseEntity.getBody());
        }
    }

}
