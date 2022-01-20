package my.education.iexcloudapidemo.dto;

import lombok.Builder;
import lombok.Data;
import my.education.iexcloudapidemo.model.Stock;

/**
 * @author Nikita Gvardeev
 * 18.01.2022
 */

@Data
@Builder
public class StockDto {

    private Long previousVolume;
    private Long volume;
    private Float latestPrice;

    public static StockDto toDto(Stock stock) {
        return StockDto
                .builder()
                .previousVolume(stock.getPreviousVolume())
                .volume(stock.getVolume())
                .latestPrice(stock.getLatestPrice())
                .build();
    }

    public static Stock toDocument(StockDto stockDto) {
        return Stock
                .builder()
                .previousVolume(stockDto.getPreviousVolume())
                .volume(stockDto.getVolume())
                .latestPrice(stockDto.getLatestPrice())
                .build();
    }
}
