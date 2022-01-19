package my.education.iexcloudapidemo.producerconsumer;

import lombok.RequiredArgsConstructor;
import my.education.iexcloudapidemo.dto.CompanyDto;
import my.education.iexcloudapidemo.dto.StockDto;
import my.education.iexcloudapidemo.repository.CompanyRepository;
import my.education.iexcloudapidemo.service.StockService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

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
public class Consumer {

    private final BlockingQueue<CompanyDto> blockingQueue;
    private final StockService stockService;
    private final CompanyRepository companyRepository;
    private final AtomicBoolean stop;

    @Async("apiExecutor")
    public void consume() {
        while (!stop.get()) {
            try {
                CompletableFuture firstConsume = asyncExecute(blockingQueue.take());
                CompletableFuture secondConsume = asyncExecute(blockingQueue.take());

            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    private CompletableFuture asyncExecute(CompanyDto company) throws InterruptedException, ExecutionException {
        CompletableFuture<CompanyDto> future = CompletableFuture
                .supplyAsync(() -> stockService.findBySymbol(company.getSymbol()))
                .thenAccept(stockDto -> company.setStockDto(stockDto));
        companyRepository.save(CompanyDto.toDocument(company));
    }
}
