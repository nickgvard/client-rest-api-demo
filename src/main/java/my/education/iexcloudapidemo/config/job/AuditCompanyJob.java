package my.education.iexcloudapidemo.config.job;

import lombok.RequiredArgsConstructor;
import my.education.iexcloudapidemo.producerconsumer.CompanyConsumer;
import my.education.iexcloudapidemo.producerconsumer.CompanyProducer;
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

    private final CompanyProducer companyProducer;
    private final CompanyConsumer companyConsumer;

    @Scheduled(fixedDelayString = "${schedule.delay.audit}")
    public void startAudit() {
        companyProducer.produce();
        companyConsumer.consume();
    }
}
