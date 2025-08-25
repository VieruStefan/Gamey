package org.dis.diffbotapi.service;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.util.retry.Retry;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class DiffbotServiceImpl implements DiffbotService {
   private static final Logger logger = LoggerFactory.getLogger(DiffbotServiceImpl.class);
   @Value("${diffbot.api.token}")
   private String apiToken;
   private final String basepath = "https://api.diffbot.com/v3/";
   private final KafkaService kafkaService;
   private static final OkHttpClient httpClient = new OkHttpClient.Builder()
         .callTimeout(60, TimeUnit.SECONDS) // Total timeout for the call
         .connectTimeout(60, TimeUnit.SECONDS) // Timeout for establishing a connection
         .readTimeout(60, TimeUnit.SECONDS) // Timeout for reading the response
         .build();
   ;

   public DiffbotServiceImpl(KafkaService kafkaService) {
      this.kafkaService = kafkaService;
   }

   @Override
   public void sendRequest(String api, String resource) {
      if (!api.equals("list")) {
         logger.error("Invalid api received. Only \"list\" API is supported.");
      }
      final String requestId = UUID.randomUUID().toString().substring(0, 8);
      resource = URLEncoder.encode(resource, StandardCharsets.UTF_8);

      StringBuilder builder = new StringBuilder(basepath);
      builder.append(api).append('?').append("token=").append(apiToken).append('&').append("url=").append(resource);
      String uri = builder.toString();
      logger.info("[{}] Starting request for uri={}", requestId, uri);

      synchronized (httpClient) {
         Request request = new Request.Builder()
               .url(uri)
               .build();
         try (Response response = httpClient.newCall(request).execute()) {
            System.out.println(response.body().string());
         } catch (IOException e) {
            logger.warn("[{}] Error during request for uri={}", requestId, uri, e);
         }
      }
   }
}
