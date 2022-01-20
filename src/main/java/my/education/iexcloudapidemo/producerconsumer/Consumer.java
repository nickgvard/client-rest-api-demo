package my.education.iexcloudapidemo.producerconsumer;

import lombok.RequiredArgsConstructor;
import my.education.iexcloudapidemo.dto.CompanyDto;
import my.education.iexcloudapidemo.dto.StockDto;
import my.education.iexcloudapidemo.service.CompanyService;
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
    private final StockService stockService;
    private final CompanyService companyService;
    private final AtomicBoolean stop;

    @Async("apiExecutor")
    public void consume() {
        while (!stop.get()) {
            try {
                CompanyDto companyDto = blockingQueue.take();

                CompletableFuture<CompanyDto> first = findBySymbol(companyDto)
                        .thenCompose(stockDto -> saveCompany(companyDto, stockDto));

                CompletableFuture<CompanyDto> second = findBySymbol(companyDto)
                        .thenCompose(stockDto -> saveCompany(companyDto, stockDto));

                CompletableFuture<Void> combine = CompletableFuture.allOf(first, second);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private CompletableFuture<StockDto> findBySymbol(CompanyDto companyDto) {
        return CompletableFuture.supplyAsync(
                () -> stockService.findBySymbolFromApi(companyDto.getSymbol()));
    }

    private CompletableFuture<CompanyDto> saveCompany(CompanyDto companyDto, StockDto stockDto) {
        return CompletableFuture.supplyAsync(() -> {
            companyDto.setStockDto(stockDto);
            return companyDto;
        }).thenApply(companyService::save);
    }
}
