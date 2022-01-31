package my.education.iexcloudapidemo.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;
import my.education.iexcloudapidemo.model.Company;


/**
 * @author Nikita Gvardeev
 * 18.01.2022
 */

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class CompanyDto {

    private Long id;
    private String symbol;
    private Boolean isEnabled;
    private Long previousVolume;
    private Long volume;
    private Float latestPrice;

    public static CompanyDto toDto(Company company) {
        return CompanyDto
                .builder()
                .id(company.getId())
                .symbol(company.getSymbol())
                .isEnabled(company.getIsEnabled())
                .previousVolume(company.getPreviousVolume())
                .volume(company.getVolume())
                .latestPrice(company.getLatestPrice())
                .build();
    }

    public static Company toEntity(CompanyDto companyDto) {
        return Company
                .builder()
                .id(companyDto.getId())
                .symbol(companyDto.getSymbol())
                .isEnabled(companyDto.getIsEnabled())
                .previousVolume(companyDto.getPreviousVolume())
                .volume(companyDto.getVolume())
                .latestPrice(companyDto.getLatestPrice())
                .build();
    }
}
