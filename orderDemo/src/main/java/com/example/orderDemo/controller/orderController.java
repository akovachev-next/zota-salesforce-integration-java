package com.example.orderDemo.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


import java.util.HashMap;
import java.util.Map;


@RestController
public class orderController {

    @Value("${salesforce.client-id}")
    private String clientId;

    @Value("${salesforce.client-secret}")
    private String clientSecret;

    @Value("${salesforce.redirect-uri}")
    private String redirectUri;

    private final String AUTH_URL = "https://login.salesforce.com/services/oauth2/authorize";
    private final String TOKEN_URL = "https://login.salesforce.com/services/oauth2/token";

    @GetMapping("/")
    public ResponseEntity<?> login(){
        String url = AUTH_URL + 
                "?response_type=code" + 
                "&client_id=" + clientId +
                "&redirect_uri=" + redirectUri;
                System.out.println(url);

        return ResponseEntity.status(HttpStatus.FOUND).header("Location", url).build();
    }

    @GetMapping("/callback")
    public String callback(@RequestParam(value = "code", required = false) String code) {

         try {
            RestTemplate restTemplate = new RestTemplate();

            String tokenUrl = TOKEN_URL;

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("grant_type", "authorization_code");
            body.add("client_id", clientId);
            body.add("client_secret", clientSecret);
            body.add("redirect_uri", redirectUri);
            body.add("code", code);

            HttpEntity<MultiValueMap<String, String>> request =
                new HttpEntity<>(body, headers);

            Map tokenResponse = restTemplate.postForObject(tokenUrl, request, Map.class);

            System.out.println("TOKEN RESPONSE: " + tokenResponse);

            String accessToken = (String) tokenResponse.get("access_token");
            String instanceUrl = (String) tokenResponse.get("instance_url");

            String apexUrl = instanceUrl + "/services/apexrest/orders";

            headers.set("Authorization", "Bearer " + accessToken);
            headers.setContentType(MediaType.APPLICATION_JSON);
        
            Map<String, Object> requestBody = new HashMap<>();
            //change value of orderId, value must be unique!
            requestBody.put("orderId", "testId-01");
            requestBody.put("customerName", "Demo costumer 01");
            requestBody.put("amount", 165.50);
            requestBody.put("status", "Completed");

            HttpEntity<Map<String, Object>> requestBodyValues = new HttpEntity<>(requestBody, headers);

            restTemplate.postForObject(apexUrl, requestBodyValues, String.class);


            String success = "Order created successfully!";
            return success;

        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR: " + e.getMessage();
        }
    }
    
    

    
}
