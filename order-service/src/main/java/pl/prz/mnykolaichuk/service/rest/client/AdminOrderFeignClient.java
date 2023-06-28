package pl.prz.mnykolaichuk.service.rest.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import pl.prz.mnykolaichuk.configuration.FeignClientConfiguration;

import java.util.HashMap;

@FeignClient(value = "keycloak-client", url = "${spring.cloud.openfeign.client.config.postClient.url}", configuration = FeignClientConfiguration.class)
public interface AdminOrderFeignClient {

    @GetMapping(path = "/auth/admin/realms/web-application-realm/users/{user-id}")
    HashMap<String, Object> getUserInfo(@PathVariable("user-id") String userId,
                                        @RequestHeader("Authorization") String authorizationHeader);
}
