package my.education.iexcloudapidemo.dto;

import lombok.Builder;
import lombok.Data;
import my.education.iexcloudapidemo.model.Company;

import java.util.Objects;

/**
 * @author Nikita Gvardeev
 * 18.01.2022
 */

@Data
@Builder
public class CompanyDto {

    private Long id;
    private String symbol;
    private Boolean isEnabled;
    private StockDto stockDto;

    public static CompanyDto toDto(Company company) {
        return CompanyDto
                .builder()
                .id(company.getId())
                .symbol(company.getSymbol())
                .isEnabled(company.getIsEnabled())
                .stockDto(Objects.nonNull(company.getStock()) ? StockDto
                        .toDto(company.getStock()) : null)
                .build();
    }

    public static Company toDocument(CompanyDto companyDto) {
        return Company
                .builder()
                .id(companyDto.getId())
                .symbol(companyDto.getSymbol())
                .isEnabled(companyDto.getIsEnabled())
                .stock(Objects.nonNull(companyDto.getStockDto()) ? StockDto
                        .toDocument(companyDto.getStockDto()) : null)
                .build();
    }
}
