package org.dis.diffbotapi.configuration;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.util.concurrent.TimeUnit;

@Configuration
public class OkHttpConfig {
   @Bean
   public OkHttpClient okHttpClient() {
      int cacheSize = 10 * 1024 * 1024;
      File cacheDirectory = new File("/cache");
      Cache cache = new Cache(cacheDirectory, cacheSize);
      return new OkHttpClient.Builder()
            .callTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .cache(cache)
            .build();
   }
}
