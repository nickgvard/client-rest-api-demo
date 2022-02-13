package my.education.iexcloudapidemo.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import my.education.iexcloudapidemo.model.Company;


/**
 * @author Nikita Gvardeev
 * 18.01.2022
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CompanyDto {

    @ToString.Exclude
    private Long id;
    private String symbol;
    private String companyName;
    @ToString.Exclude
    private Boolean isEnabled;
    @ToString.Exclude
    private Long previousVolume;
    @ToString.Exclude
    private Long volume;
    @ToString.Exclude
    private Float latestPrice;

    public static CompanyDto toDto(Company company) {
        return CompanyDto
                .builder()
                .id(company.getId())
                .symbol(company.getSymbol())
                .companyName(company.getCompanyName())
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
                .companyName(companyDto.getCompanyName())
                .isEnabled(companyDto.getIsEnabled())
                .previousVolume(companyDto.getPreviousVolume())
                .volume(companyDto.getVolume())
                .latestPrice(companyDto.getLatestPrice())
                .build();
    }
}
