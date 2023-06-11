package org.thoughtworks.wayoflearning;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableFeignClients
@EnableJpaRepositories
@EnableJpaAuditing
public class WayOfLearningApplication {

    public static void main(String[] args) {
        SpringApplication.run(WayOfLearningApplication.class, args);
    }

}
