package pl.prz.mnykolaichuk.configuration;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import java.util.Locale;

@Configuration
public class AppConfiguration {

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource
                = new ReloadableResourceBundleMessageSource();
        messageSource.setDefaultLocale(Locale.UK);
        messageSource.setBasenames(
                "classpath:/messages/error-messages",
                "classpath:/messages/success-messages"
        );
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
}
