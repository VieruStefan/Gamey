package org.dis.reducer.controller;

import org.dis.reducer.service.ReducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/sse")
@CrossOrigin("http://localhost:3000")
public class SseController {
   
   @Autowired
   ReducerService reducerService;
   
   @GetMapping("/stream")
   public Flux<ServerSentEvent<String>> streamEvents() {
//      return Flux.interval(Duration.ofSeconds(10))
      AtomicInteger i = new AtomicInteger(0);
      return reducerService.getUpdateStream()
                 .map(productJson -> ServerSentEvent.<String>builder()
                                                 .id(String.valueOf(i.addAndGet(1)))
                                                 .event("product-update")
                                                 .data(productJson)
                                                 .build());
   }
}
