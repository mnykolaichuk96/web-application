package pl.prz.mnykolaichuk.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

import java.security.Principal;

@RestController
@RequestMapping("/api")
public class TestController {

    @GetMapping("/anonymous")
    public ResponseEntity<String> getAnonymous(ServerWebExchange exchange) {
        return ResponseEntity.ok("Hello Anonymous");
    }

    @GetMapping("/admin")
    public ResponseEntity<String> getAdmin(Principal principal) {

        return ResponseEntity.ok("Hello Admin \nUser Name : "  + "\nUser Email : " );
    }

    @GetMapping("/user")
    public ResponseEntity<String> getUser(@RequestHeader HttpHeaders headers,
                                          HttpServletRequest request) {

        return ResponseEntity.ok("Hello User \nUser Name : "  + "\nUser Email : " );
    }

}
