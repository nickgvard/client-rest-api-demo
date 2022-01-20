package my.education.iexcloudapidemo.service.impl;

import lombok.RequiredArgsConstructor;
import my.education.iexcloudapidemo.dto.StockDto;
import my.education.iexcloudapidemo.model.Stock;
import my.education.iexcloudapidemo.service.StockService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author Nikita Gvardeev
 * 17.01.2022
 */

@Service
@RequiredArgsConstructor
public class StockServiceImpl implements StockService {

    @Value("${iexcloud.stock}")
    private String urlApi;
    private final RestTemplate restTemplate;

    @Override
    public StockDto findBySymbolFromApi(String symbol) {
        Map<String, String> param = new HashMap<>();
        param.put("stockCode", symbol);

        Stock stock = restTemplate.getForObject(urlApi, Stock.class, param);

        if (Objects.isNull(stock))
            throw new RuntimeException("API is invalid");

        return StockDto.toDto(stock);
    }
}
