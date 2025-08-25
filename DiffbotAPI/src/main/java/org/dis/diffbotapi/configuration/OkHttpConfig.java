package org.dis.diffbotapi.configuration;

import okhttp3.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.util.concurrent.TimeUnit;

@Configuration
public class OkHttpConfig {
   @Bean
   public OkHttpClient okHttpClient() {
      int cacheSize = 1000 * 1024 * 1024;
      File cacheDirectory = new File("okhttp-cache");
      if (!cacheDirectory.exists()) {
         cacheDirectory.mkdirs();
      }
      Cache cache = new Cache(cacheDirectory, cacheSize);
      Interceptor cacheInterceptor = chain -> {
         Response originalResponse = chain.proceed(chain.request());
         CacheControl cacheControl = new CacheControl.Builder()
               .maxAge(21, TimeUnit.DAYS)
               .build();

         return originalResponse.newBuilder()
               .header("Cache-Control", cacheControl.toString())
               .build();
      };

      return new OkHttpClient.Builder()
            .callTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .cache(cache)
            .addNetworkInterceptor(cacheInterceptor)
            .build();
   }
}
