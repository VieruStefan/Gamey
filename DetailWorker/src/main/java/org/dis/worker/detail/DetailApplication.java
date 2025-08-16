package org.dis.worker.detail;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
class DetailApplication {

    public static void main(String[] args) {
        SpringApplication.run(DetailApplication.class, args);
    }

}
