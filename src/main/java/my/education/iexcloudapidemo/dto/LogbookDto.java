package my.education.iexcloudapidemo.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import my.education.iexcloudapidemo.model.Logbook;

import java.util.Objects;

/**
 * @author Nikita Gvardeev
 * 22.01.2022
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class LogbookDto {

    @ToString.Exclude
    private Long id;
    private Float oldPrice;
    private Float currentPrice;
    private CompanyDto companyDto;

    public static LogbookDto toDto(Logbook logbook) {
        return LogbookDto
                .builder()
                .id(logbook.getId())
                .oldPrice(logbook.getOldPrice())
                .currentPrice(logbook.getCurrentPrice())
                .companyDto(Objects.nonNull(logbook.getCompany())
                        ? CompanyDto.toDto( logbook.getCompany())
                        : null)
                .build();
    }

    public static Logbook toEntity(LogbookDto logbookDto) {
        return Logbook
                .builder()
                .id(logbookDto.getId())
                .oldPrice(logbookDto.getOldPrice())
                .currentPrice(logbookDto.getCurrentPrice())
                .company(Objects.nonNull(logbookDto.getCompanyDto())
                        ? CompanyDto.toEntity(logbookDto.getCompanyDto())
                        : null)
                .build();
    }
}
