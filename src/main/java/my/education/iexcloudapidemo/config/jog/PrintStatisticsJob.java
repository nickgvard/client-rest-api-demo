package my.education.iexcloudapidemo.config.jog;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.education.iexcloudapidemo.service.CompanyService;
import my.education.iexcloudapidemo.service.LogbookService;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * @author Nikita Gvardeev
 * 27.01.2022
 */

@Configuration
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class PrintStatisticsJob {

    private final CompanyService companyService;
    private final LogbookService logbookService;

    @Scheduled(fixedDelayString = "${schedule.delay.print}")
    public void startPrint() {
        log.info(companyService.findTop5CompaniesAndOther().toString());
        log.info(logbookService.findTop5ByDeltaLatestPrice().toString());
    }
}
