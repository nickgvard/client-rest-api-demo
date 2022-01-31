package my.education.iexcloudapidemo.config;

import my.education.iexcloudapidemo.dto.CompanyDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author Nikita Gvardeev
 * 18.01.2022
 */

@Configuration
@EnableAsync
public class Config {

    @Value("${threads.max.pool.size}")
    private Integer threads;
    @Value("${threads.core.pool.size}")
    private Integer coreThreads;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean("apiExecutor")
    public Executor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(coreThreads);
        executor.setMaxPoolSize(threads);
        executor.setThreadNamePrefix("APIExecutor-");
        executor.initialize();
        return executor;
    }

    @Bean
    public BlockingQueue<CompanyDto> blockingQueue() {
        return new LinkedBlockingQueue<>();
    }
}
