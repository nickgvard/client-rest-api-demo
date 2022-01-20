package my.education.iexcloudapidemo.service;

import my.education.iexcloudapidemo.dto.StockDto;

/**
 * @author Nikita Gvardeev
 * 19.01.2022
 */

public interface StockService {

    StockDto findBySymbolFromApi(String symbol);

}
