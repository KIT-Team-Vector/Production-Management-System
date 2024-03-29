package edu.kit.ordermanager.services.rest;

import edu.kit.ordermanager.entities.ResourceSet;
import edu.kit.ordermanager.handlers.IRestServiceHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Component
public class RestServiceHandler implements IRestServiceHandler {

    RestTemplate restTemplate;

    public RestServiceHandler(@Autowired RestTemplate restTemplate) {
       this.restTemplate = restTemplate;
    }


    @Override
    public <T> T sendGetRequest(String url, Class<T> responseType, Object uriVariables) {
        ResponseEntity<T> responseEntity;
        if(uriVariables == null) {
            responseEntity = restTemplate.getForEntity(url, responseType);

            System.out.println(responseEntity.getStatusCode().value());

            if (responseEntity.getStatusCode() != HttpStatus.OK) {
                return null;
            }
        } else {
            responseEntity = restTemplate.getForEntity(url, responseType, uriVariables);
        }
        return responseEntity.getBody();
    }

    @Override
    public <T> T sendPostRequest(String url, Object request, Class<T> responseType) throws RestClientException {
        ResponseEntity<T> responseEntity;
        if(request == null) {
            responseEntity = restTemplate.postForEntity(url, null, responseType);

            System.out.println(responseEntity.getStatusCode().value());

            if (responseEntity.getStatusCode() != HttpStatus.OK) {
                return null;
            }
        } else {
            responseEntity = restTemplate.postForEntity(url, request, responseType);
        }
        return responseEntity.getBody();
    }


}
