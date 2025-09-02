package org.dis.diffbotapi.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Service
public class DiffbotServiceImpl implements DiffbotService {
   private static final Logger logger = LoggerFactory.getLogger(DiffbotServiceImpl.class);
   private final ObjectMapper objectMapper = new ObjectMapper();
   private final OkHttpClient okHttpClient;
   @Value("${diffbot.api.token}")
   private String apiToken;
   private final String basepath = "https://api.diffbot.com/v3/";
   private final KafkaService kafkaService;


   public DiffbotServiceImpl(KafkaService kafkaService, OkHttpClient okHttpClient) {
      this.kafkaService = kafkaService;
      this.okHttpClient = okHttpClient;
   }

   private String generateUrl(String path) {
      HttpUrl.Builder urlBuilder
            = Objects.requireNonNull(HttpUrl.parse(basepath + "list")).newBuilder();
      urlBuilder.addQueryParameter("token", this.apiToken);
      urlBuilder.addQueryParameter("url", path);

      return urlBuilder.toString();
   }

   @Override
   public synchronized void sendRequest(String jobId, String resource) {
      final String requestId = UUID.randomUUID().toString().substring(0, 8);
      String url = generateUrl(resource);
      Request request = new Request.Builder()
            .url(url)
            .build();
      Call call = okHttpClient.newCall(request);
      logger.info("[{}] Starting request for uri={}", requestId, url);
      try (Response response = call.execute()) {
         String responseBody = response.body().string();
         logger.info("[{}] Completed request for uri={} with body={}", requestId, url, responseBody.substring(0, 128));
         Map<String, String> apiResponse = new HashMap<>();
         apiResponse.put("products", responseBody);
         apiResponse.put("jobId", jobId);
         kafkaService.sendMessage("api.responses", objectMapper.writeValueAsString(apiResponse));
      } catch (IOException e) {
         logger.warn("[{}] Error during request for uri={}", requestId, url, e);
      }
   }
}
