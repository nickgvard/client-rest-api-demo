package my.education.iexcloudapidemo.service;

import my.education.iexcloudapidemo.dto.CompanyDto;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author Nikita Gvardeev
 * 19.01.2022
 */
public interface CompanyService {

    CompletableFuture<List<CompanyDto>> findAll();

    CompletableFuture<CompanyDto> findStockByCompany(CompanyDto company);

    CompletableFuture<List<CompanyDto>> saveAll(List<CompanyDto> companies);

    CompletableFuture<List<CompanyDto>> findTop5CompaniesAndOther();
}
