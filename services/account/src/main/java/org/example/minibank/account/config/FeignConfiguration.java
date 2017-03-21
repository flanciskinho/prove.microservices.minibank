package org.example.minibank.account.config;

import feign.Logger;
import feign.Request;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by flanciskinho on 20/3/17.
 */
@Configuration
public class FeignConfiguration {

    public static final int FIVE_SECONDS = 5000;

    @Bean
    public Logger.Level feignLogger() {
        return Logger.Level.FULL;
    }

    @Bean
    public Request.Options options() {
        return new Request.Options(FIVE_SECONDS, FIVE_SECONDS);
    }
}

