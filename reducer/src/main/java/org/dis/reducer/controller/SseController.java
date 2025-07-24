package org.dis.reducer.controller;

import org.dis.reducer.service.ReducerService;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class SseController {
   
   private final ReducerService updateService;
   
   // Inject the service that provides the stream of Kafka messages
   public SseController(ReducerService updateService) {
      this.updateService = updateService;
   }
   
   @GetMapping(value = "/api/events", produces = "text/event-stream")
   public Flux<ServerSentEvent<String>> handleSse() {
      // 1. Get the stream of product updates from our service
      return updateService.getUpdateStream()
                                 // 2. Map each product JSON string into a ServerSentEvent
                                 .map(productJson -> ServerSentEvent.<String>builder()
                                                                    .event("product-update") // A custom event name
                                                                    .data(productJson)
                                                                    .build());
   }
}
