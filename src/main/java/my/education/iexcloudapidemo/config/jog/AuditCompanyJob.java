package my.education.iexcloudapidemo.config.jog;

import lombok.RequiredArgsConstructor;
import my.education.iexcloudapidemo.producerconsumer.Consumer;
import my.education.iexcloudapidemo.producerconsumer.Producer;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * @author Nikita Gvardeev
 * 25.01.2022
 */

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class AuditCompanyJob {

    private final Producer producer;
    private final Consumer consumer;

    @Scheduled(fixedDelayString = "${schedule.delay.audit}")
    public void startAudit() {
        producer.produce();
        consumer.consume();
    }
}
