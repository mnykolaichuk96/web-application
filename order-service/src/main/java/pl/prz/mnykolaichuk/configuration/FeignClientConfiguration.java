package pl.prz.mnykolaichuk.configuration;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.prz.mnykolaichuk.service.rest.client.InventoryFeignClient;

@Configuration
public class FeignClientConfiguration {

        @Bean
        Logger.Level feignLoggerLevel() {
            return Logger.Level.FULL;
        }

    }

