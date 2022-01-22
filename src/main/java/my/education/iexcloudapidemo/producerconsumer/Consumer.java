package my.education.iexcloudapidemo.producerconsumer;

import lombok.RequiredArgsConstructor;
import my.education.iexcloudapidemo.dto.CompanyDto;
import my.education.iexcloudapidemo.dto.LogbookDto;
import my.education.iexcloudapidemo.dto.StockDto;
import my.education.iexcloudapidemo.model.Logbook;
import my.education.iexcloudapidemo.service.CompanyService;
import my.education.iexcloudapidemo.service.LogbookService;
import my.education.iexcloudapidemo.service.StockService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

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
    private final AtomicBoolean stop;

    @Async("apiExecutor")
    public void consume() {
        while (!stop.get()) {
            try {
                CompletableFuture<Void> first = saveCompany(blockingQueue.take());
                CompletableFuture<Void> second = saveCompany(blockingQueue.take());

                CompletableFuture.allOf(first, second);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private CompletableFuture<Void> saveCompany(CompanyDto companyDto) {
        return CompletableFuture.runAsync(() ->
                companyService.save(companyDto)
                        .thenCompose(this::saveLogbook));
    }

    private CompletableFuture<Void> saveLogbook(CompanyDto companyDto) {
        return CompletableFuture.runAsync(() -> logbookService.save(
                LogbookDto.builder()
                        .symbol(companyDto.getSymbol())
                        .currentPrice(companyDto.getStockDto().getLatestPrice())
                        .build()));
    }
}
