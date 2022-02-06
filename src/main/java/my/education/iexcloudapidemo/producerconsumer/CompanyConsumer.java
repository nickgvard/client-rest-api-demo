package my.education.iexcloudapidemo.producerconsumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.education.iexcloudapidemo.dto.CompanyDto;
import my.education.iexcloudapidemo.service.CompanyService;
import my.education.iexcloudapidemo.service.LogbookService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author Nikita Gvardeev
 * 17.01.2022
 */

@Component
@RequiredArgsConstructor
@Slf4j
public class CompanyConsumer {

    private final AtomicInteger count;
    private final BlockingQueue<CompanyDto> blockingQueue;
    private final CompanyService companyService;
    private final LogbookService logbookService;

    @Value("${spring.jpa.properties.hibernate.jdbc.batch_size}")
    private int batchSize;

    public void consume() {
        AtomicInteger i = new AtomicInteger(0);
        List<CompanyDto> batch = new ArrayList<>();
        CompanyDto currentCompany;
        try {
            while (true) {
                currentCompany = blockingQueue.poll(10, TimeUnit.SECONDS);
                batch.add(currentCompany);
                i.getAndIncrement();
                if (batch.size() >= batchSize || blockingQueue.isEmpty()) {
                    CompletableFuture<List<CompanyDto>> futureForEachCompany = asyncRequestForEachCompany(batch);
                    batchSaveCompany(futureForEachCompany);
                    batch.clear();
                    if (i.get() >= count.get()) break;
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public CompletableFuture<List<CompanyDto>> asyncRequestForEachCompany(List<CompanyDto> companies) {
        List<CompletableFuture<CompanyDto>> findByCompanies = companies.stream()
                .map(companyDto -> {
                    try {
                        TimeUnit.MILLISECONDS.sleep(10);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    return companyService.findStockByCompany(companyDto);
                })
                .collect(Collectors.toList());

        CompletableFuture<Void> allOfFutures = CompletableFuture
                .allOf(findByCompanies.toArray(new CompletableFuture[0]));

        return allOfFutures.thenApply(v ->
                findByCompanies.stream()
                        .map(CompletableFuture::join)
                        .collect(Collectors.toList()));
    }

    public void batchSaveCompany(CompletableFuture<List<CompanyDto>> allFindByCompanies) {
        allFindByCompanies.thenApply(companyService::saveAll)
                .thenApply(CompletableFuture::join)
                .thenAccept(logbookService::saveAll);
    }
}
