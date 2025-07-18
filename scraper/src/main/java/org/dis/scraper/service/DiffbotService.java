package org.dis.scraper.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

@Service
public class DiffbotService
{
   @Value("${diffbot.api.token}")
   private String apiToken;
   String basepath = "https://api.diffbot.com/v3/";
   
   public void sendRequest(String api, String resource)
   {
      String uri = UriComponentsBuilder.fromUriString(basepath)
                                       .path(api)
                                       .queryParam("token", apiToken)
                                       .queryParam("url", resource)
                                       .build().toUriString();
      WebClient client = WebClient.create();
      client.get().uri(uri).accept(MediaType.APPLICATION_JSON).retrieve().bodyToMono(String.class)
            .subscribe(
            response ->
            {
               System.out.println(response);
            },
            error ->
            {
               System.err.println(error);
            },
            () ->
            {
               System.out.println("Api call done");
            }
            );
   }
}
