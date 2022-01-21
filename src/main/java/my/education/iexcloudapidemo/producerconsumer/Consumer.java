package my.education.iexcloudapidemo.producerconsumer;

import lombok.RequiredArgsConstructor;
import my.education.iexcloudapidemo.dto.CompanyDto;
import my.education.iexcloudapidemo.dto.StockDto;
import my.education.iexcloudapidemo.service.CompanyService;
import my.education.iexcloudapidemo.service.StockService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

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

                CompletableFuture<CompanyDto> first = saveCompany(blockingQueue.take());

                CompletableFuture<CompanyDto> second = saveCompany(blockingQueue.take());

                CompletableFuture.allOf(first, second);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private CompletableFuture<CompanyDto> saveCompany(CompanyDto companyDto) {
        Supplier<StockDto> findBySymbol = () -> stockService.findBySymbolFromApi(companyDto.getSymbol());
        return CompletableFuture.supplyAsync(findBySymbol)
                .thenApply(stockDto -> {
                    companyDto.setStockDto(stockDto);
                    return companyDto;
                })
                .thenCompose(companyService::save)
                .handle((dto, throwable) -> {
                    if (Objects.isNull(dto))
                        throw new RuntimeException(throwable.getMessage());
                    else
                        return dto;
                });
    }
}
