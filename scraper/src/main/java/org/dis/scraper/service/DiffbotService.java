package org.dis.scraper.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class DiffbotService
{
   @Value("${diffbot.api.token}")
   private String apiToken;
   String basepath = "https://api.diffbot.com/v3/";
   
   KafkaService kafkaService;
   
   @Autowired
   public DiffbotService(KafkaService kafkaService)
   {
      this.kafkaService = kafkaService;
   }
   
   public void sendRequest(String api, String resource)
   {
      String uri = UriComponentsBuilder.fromUriString(basepath)
                                       .path(api)
                                       .queryParam("token", apiToken)
                                       .queryParam("url", resource)
                                       .build().toUriString();
      WebClient client = WebClient.create();
      client.get().uri(uri).accept(MediaType.APPLICATION_JSON).retrieve().bodyToMono(String.class).subscribe(
      response ->
      {
         kafkaService.sendMessage("web-scraping-" + api + "-output", response);
         System.out.println("Response sent to web-scraping-" + api + "-output");
      }, error ->
      {
         System.err.println(error);
      }, () ->
      {
         System.out.println("Api call done");
      });
   }
}
