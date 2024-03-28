package org.glazweq.demo.configs;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
@Configuration
public class RestTemplateConfig {
//    @Bean
//    public RestTemplate restTemplate(){
//        return new RestTemplate();
//    }
    @Bean
    public RestTemplate restTemplateTest(RestTemplateBuilder restTemplateBuilder){
        return restTemplateBuilder.build();
    }
}
