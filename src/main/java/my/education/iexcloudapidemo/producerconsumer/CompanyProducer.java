package my.education.iexcloudapidemo.producerconsumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.education.iexcloudapidemo.dto.CompanyDto;
import my.education.iexcloudapidemo.service.CompanyService;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Nikita Gvardeev
 * 17.01.2022
 */

@Component
@RequiredArgsConstructor
@Slf4j
public class CompanyProducer {

    private final AtomicInteger count;
    private final BlockingQueue<CompanyDto> blockingQueue;
    private final CompanyService companyService;

    public void produce() {
        companyService.findAll()
                .thenAccept(companies -> {
                    try {
                        int i = 0;
                        for (CompanyDto company : companies) {
                            if (company.getIsEnabled()) {
                                blockingQueue.put(company);
                            }
                            count.set(i++);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
    }
}
