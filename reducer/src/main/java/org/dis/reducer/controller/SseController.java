package org.dis.reducer.controller;

import org.dis.reducer.service.ReducerService;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/sse")
@CrossOrigin("http://localhost:3000")
public class SseController {
   
   ReducerService reducerService;
   private final AtomicInteger i = new AtomicInteger(0);
   Flux<ServerSentEvent<String>> heartbeat = Flux.interval(Duration.ofSeconds(9))
                                                 .map(m ->
                                                      ServerSentEvent.<String>builder()
                                                                     .event("heartbeat")
                                                                     .data("keep-alive")
                                                                     .build()
                                                 );
   
   public SseController(ReducerService reducerService)
   {
      this.reducerService = reducerService;
   }
   
   @GetMapping("/stream")
   public Flux<ServerSentEvent<String>> streamEvents() {
      return reducerService.getUpdateSink()
                           .asFlux()
//                           .delayElements(Duration.ofMillis(500))
                           .map(productJson ->
                                ServerSentEvent.<String>builder()
                                               .id(String.valueOf(i.addAndGet(1)))
                                               .event("product-update")
                                               .data(productJson)
                                               .build()
                           )
                           .mergeWith(heartbeat);
   }
}
