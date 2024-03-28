package org.glazweq.demo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

@Service
@Slf4j
public class FilmApiService {
    private static final  String url = "https://api.kinopoisk.dev/v1.4/movie/";
    private static final String header1 = "H1CX9MG-T83MTC4-PPV7CQE-SYP1474";
    private static final String header2 = "application/json";

    @Autowired
    private RestTemplate restTemplate;



    public String getFirstTenMovie(String filmId){
        try {
            //header value is set
            HttpHeaders headers = new HttpHeaders();

            headers.set("X-API-KEY", header1);
            headers.set("accept", header2);
            //make GET  call
            String request = url +filmId;
            System.out.println(request);
            ResponseEntity<String> response = restTemplate.exchange(url + filmId, HttpMethod.GET,new HttpEntity<>(headers),String.class);

            log.info("Output from KinoApi:{}", response.getBody());

            return response.getBody();

        }catch (Exception e){
            log.error("something went wrong while getting value from KINOAPI", e);
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Exception while calling end poing from KinoApi",
                    e
            );
        }
    }
}
