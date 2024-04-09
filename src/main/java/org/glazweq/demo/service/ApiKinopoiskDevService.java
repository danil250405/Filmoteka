package org.glazweq.demo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
@Service
@Slf4j
public class ApiKinopoiskDevService {

    /*
    keys: D386SAK-6YR4GXR-KYNQ26E-0JHQX0C batya
          H1CX9MG-T83MTC4-PPV7CQE-SYP1474 my
          90T8CSE-7P34EBK-HHTP44M-DA7KSDG carhartt
          27AG916-69D40PB-N6WACS4-H90QVSE kolyan
          H659JZ2-ZV64B13-QQK91XR-T33YW3Q mama
          8HK60K1-GN54HEM-NA6DYES-5HQBVWJ vitalya
          Y8EGHT0-CSZ4PGE-QZ82S6C-KN2CVA7 sonya
     */
    private static final String header1 = "H1CX9MG-T83MTC4-PPV7CQE-SYP1474";
    private static final String header2 = "application/json";

    @Autowired
    private RestTemplate restTemplate;
    public String getResponceByRequest(String request){
        try {
            //header value is set
            HttpHeaders headers = new HttpHeaders();

            headers.set("X-API-KEY", header1);
            headers.set("accept", header2);
            //make GET  call
            ResponseEntity<String> response = restTemplate.exchange(request, HttpMethod.GET,new HttpEntity<>(headers),String.class);

            log.info("Output from KinoApi:{}", response.getBody());
            System.out.println("REQUEST: "+ request);
            System.out.println("RESPONSE: "+ response);
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
    @Cacheable(cacheNames = "apiResponseCache", key = "#requestFromController")
    public JsonNode getResponseFromApi(String requestFromController) throws JsonProcessingException {
        String answerFromApi = getResponceByRequest(requestFromController);
        System.out.println("get Response from api:" + answerFromApi);
        return JsonDecoderService.parse(answerFromApi);
    }

}
