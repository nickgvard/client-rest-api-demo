package my.education.iexcloudapidemo.config;

import my.education.iexcloudapidemo.dto.CompanyDto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Nikita Gvardeev
 * 18.01.2022
 */

@Configuration
@EnableAsync
public class Config {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public BlockingQueue<CompanyDto> blockingQueue() {
        return new LinkedBlockingQueue<>();
    }

    @Bean
    public AtomicInteger count() {
        return new AtomicInteger(0);
    }
}
