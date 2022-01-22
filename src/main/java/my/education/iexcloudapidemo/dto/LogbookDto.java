package my.education.iexcloudapidemo.dto;

import lombok.Builder;
import lombok.Data;
import my.education.iexcloudapidemo.model.Logbook;

import java.time.LocalDate;

/**
 * @author Nikita Gvardeev
 * 22.01.2022
 */

@Data
@Builder
public class LogbookDto {

    private String id;
    private String symbol;
    private Float oldPrice;
    private Float currentPrice;
    private LocalDate registry;

    public static LogbookDto toDto(Logbook logbook) {
        return LogbookDto
                .builder()
                .id(logbook.getId())
                .symbol(logbook.getSymbol())
                .oldPrice(logbook.getOldPrice())
                .currentPrice(logbook.getCurrentPrice())
                .registry(logbook.getRegistry())
                .build();
    }

    public static Logbook toDocument(LogbookDto logbookDto) {
        return Logbook
                .builder()
                .id(logbookDto.getId())
                .symbol(logbookDto.getSymbol())
                .oldPrice(logbookDto.getOldPrice())
                .currentPrice(logbookDto.getCurrentPrice())
                .registry(logbookDto.getRegistry())
                .build();
    }
}
