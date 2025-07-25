package org.dis.master.controller;

import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.LocalTime;

@RestController
@RequestMapping("/sse")
@CrossOrigin("http://localhost:3000")
public class SseController {
   
   @GetMapping("/stream")
   public Flux<ServerSentEvent<String>> streamEvents() {
      // Flux.interval creates a stream that emits a new value every second.
      return Flux.interval(Duration.ofSeconds(10))
                 .map(sequence -> ServerSentEvent.<String>builder()
                                                 .id(String.valueOf(sequence))
                                                 .event("periodic-update")
                                                 .data("SSE Update at " + LocalTime.now().toString())
                                                 .comment("This is a keep-alive comment")
                                                 .build());
   }
}