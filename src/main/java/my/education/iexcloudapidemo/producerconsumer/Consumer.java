package my.education.iexcloudapidemo.producerconsumer;

import lombok.RequiredArgsConstructor;
import my.education.iexcloudapidemo.dto.CompanyDto;
import my.education.iexcloudapidemo.service.CompanyService;
import my.education.iexcloudapidemo.service.LogbookService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;

/**
 * @author Nikita Gvardeev
 * 17.01.2022
 */

@Component
@RequiredArgsConstructor
public class Consumer {

    private final BlockingQueue<CompanyDto> blockingQueue;
    private final CompanyService companyService;
    private final LogbookService logbookService;

    @Value("${spring.jpa.properties.hibernate.jdbc.batch_size}")
    private int batchSize;

    public void consume() {
        List<CompanyDto> batch = new ArrayList<>();
        int i = 0;
        while (!blockingQueue.isEmpty()) {
            try {
                CompanyDto take = blockingQueue.take();
                batch.add(take);
                if (i++ != batchSize || blockingQueue.size() == 0) {
                    CompletableFuture<List<CompanyDto>> asyncRequest = asyncRequestForEachCompany(batch);
                    asyncRequest.thenAccept(companies -> {
                        batchSaveToCompany(companies);
                        batchSaveToLogbook(companies);
                    });
                    batch.clear();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private CompletableFuture<List<CompanyDto>> asyncRequestForEachCompany(List<CompanyDto> companies) {
        return CompletableFuture.supplyAsync(() -> {
            companies.forEach(companyService::findStockByCompanyFromApi);
            return companies;
        });
    }

    private void batchSaveToCompany(List<CompanyDto> companies) {
        CompletableFuture.runAsync(() -> companyService.saveAll(companies));
    }

    private void batchSaveToLogbook(List<CompanyDto> companies) {
        CompletableFuture.runAsync(() -> logbookService.saveAll(companies));
    }
}
