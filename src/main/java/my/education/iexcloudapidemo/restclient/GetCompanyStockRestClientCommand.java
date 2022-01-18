package my.education.iexcloudapidemo.restclient;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import my.education.iexcloudapidemo.dto.CompanyDto;
import my.education.iexcloudapidemo.dto.StockDto;
import my.education.iexcloudapidemo.model.Stock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * @author Nikita Gvardeev
 * 17.01.2022
 */

@Component
@RequiredArgsConstructor
public class GetCompanyStockRestClientCommand implements RestClientCommand<CompanyDto> {

    @Value("${iexcloud.stock}")
    private String urlApi;

    private final RestTemplate restTemplate;
    @Setter private CompanyDto company;

    @Async
    @Override
    public CompletableFuture<CompanyDto> executeAsync() {
        return CompletableFuture.completedFuture(withStock());
    }

    private CompanyDto withStock() {
        Map<String, String> param = new HashMap<>();
        param.put("stockCode", company.getSymbol());

        Stock stock = restTemplate.getForObject(urlApi, Stock.class, param);

        if (Objects.nonNull(stock))
            company.setStockDto(StockDto.toDto(stock));

        return company;
    }
}
