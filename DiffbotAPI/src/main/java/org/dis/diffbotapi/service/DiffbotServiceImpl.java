package org.dis.diffbotapi.service;

import okhttp3.*;
import org.jetbrains.annotations.NotNull;
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

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class DiffbotServiceImpl implements DiffbotService {
   private static final Logger logger = LoggerFactory.getLogger(DiffbotServiceImpl.class);
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
            = HttpUrl.parse(basepath + "list").newBuilder();
      urlBuilder.addQueryParameter("token", this.apiToken);
      urlBuilder.addQueryParameter("url", path);

      String result = URLEncoder.encode(path, StandardCharsets.UTF_8);

      StringBuilder builder = new StringBuilder(basepath);
      builder.append("list").append('?').append("token=").append(apiToken).append('&').append("url=").append(result);
      logger.info("builder={}", builder);
      logger.info("urlBuilder={}", urlBuilder);
      logger.info("equals={}", urlBuilder.toString().contentEquals(builder));
      return builder.toString();
   }

   @Override
   public void sendRequest(String api, String resource) {
      if (!api.equals("list")) {
         logger.error("Invalid api received. Only \"list\" API is supported.");
      }
      final String requestId = UUID.randomUUID().toString().substring(0, 8);
      String url = generateUrl(resource);

      Request request = new Request.Builder()
            .url(url)
            .build();
      synchronized (okHttpClient) {
         Call call = okHttpClient.newCall(request);
         logger.info("[{}] Starting request for uri={}", requestId, url);
         try (Response response = call.execute()) {
            kafkaService.sendMessage("api.responses", response.body().string());
            logger.info("[{}] Completed request for uri={} with body={}", requestId, url, response.body().string().substring(0, 128));
         } catch (IOException e) {
            logger.warn("[{}] Error during request for uri={}", requestId, url, e);
         }
      }
   }
}
