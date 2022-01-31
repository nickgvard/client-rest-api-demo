package my.education.iexcloudapidemo.producerconsumer;

import lombok.RequiredArgsConstructor;
import my.education.iexcloudapidemo.dto.CompanyDto;
import my.education.iexcloudapidemo.service.CompanyService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Nikita Gvardeev
 * 17.01.2022
 */

@Component
@RequiredArgsConstructor
public class Producer {

    private final BlockingQueue<CompanyDto> blockingQueue;
    private final CompanyService companyService;

    @Async("apiExecutor")
    public void produce() {
        CompletableFuture.supplyAsync(() -> companyService.findAllFromApi()
                .thenAccept(companies -> {
                    for (CompanyDto company : companies) {
                        try {
                            if (company.getIsEnabled()) {
                                blockingQueue.put(company);
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }));

    }
}
