package pl.prz.mnykolaichuk.configuration;

import feign.Logger;
import feign.RequestInterceptor;
import lombok.RequiredArgsConstructor;
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

