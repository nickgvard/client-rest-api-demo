package my.education.iexcloudapidemo.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.education.iexcloudapidemo.dto.CompanyDto;
import my.education.iexcloudapidemo.model.Company;
import my.education.iexcloudapidemo.repository.CompanyRepository;
import my.education.iexcloudapidemo.service.CompanyService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Nikita Gvardeev
 * 17.01.2022
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class CompanyServiceImpl implements CompanyService {

    @Value("${iexcloud.url.companies}")
    private String apiCompanies;

    @Value("${iexcloud.url.stock}")
    private String urlStockApi;

    @Value("${iexcloud.stock.code}")
    private String stockKeyCode;

    private final RestTemplate restTemplate;
    private final CompanyRepository repository;

    @Async
    @Override
    public CompletableFuture<List<CompanyDto>> findAll() {
        ResponseEntity<List<CompanyDto>> responseEntity = restTemplate
                .exchange(
                        apiCompanies,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<>() {
                        });

        List<CompanyDto> companies = responseEntity.getBody();

        if (Objects.isNull(companies))
            log.error("The API response returned is null");
        return CompletableFuture.completedFuture(companies);
    }

    @Async
    @Override
    public CompletableFuture<CompanyDto> findStockByCompany(CompanyDto company) {
        return CompletableFuture.supplyAsync(() -> {
            Map<String, String> param = new HashMap<>();
            param.put(stockKeyCode, company.getSymbol());
            ResponseEntity<CompanyDto> response;
            try {
                response = restTemplate.getForEntity(urlStockApi, CompanyDto.class, param);
                CompanyDto fromApi = response.getBody();
                if (Objects.nonNull(fromApi)) {
                    company.setCompanyName(fromApi.getCompanyName());
                    company.setPreviousVolume(fromApi.getPreviousVolume());
                    company.setVolume(fromApi.getVolume());
                    company.setLatestPrice(fromApi.getLatestPrice());
                }
            } catch (Exception e) {
                log.error("Symbol: {}, {}", company.getSymbol(), e.getMessage());
            }
            return company;
        });
    }

    @Async
    @Override
    public CompletableFuture<List<CompanyDto>> saveAll(List<CompanyDto> companies) {
        return CompletableFuture.supplyAsync(() -> {
            List<Company> forSave = companies
                    .stream()
                    .map(CompanyDto::toEntity)
                    .collect(Collectors.toList());

            List<Company> persists = forSave.stream().map(company -> {
                Company bySymbol = repository.findBySymbol(company.getSymbol());
                if (Objects.nonNull(bySymbol)) {
                    bySymbol.setIsEnabled(company.getIsEnabled());
                    bySymbol.setLatestPrice(company.getLatestPrice());
                    bySymbol.setPreviousVolume(company.getPreviousVolume());
                    bySymbol.setVolume(company.getVolume());
                    return repository.save(bySymbol);
                } else
                    return repository.save(company);
            }).collect(Collectors.toList());

            return persists
                    .stream()
                    .map(CompanyDto::toDto)
                    .collect(Collectors.toList());
        });
    }

    @Async
    @Override
    public CompletableFuture<List<CompanyDto>> findTop5CompaniesAndOther() {
        List<CompanyDto> topFive = repository
                .findTop5By(
                        Sort.by(Sort.Direction.DESC, "previousVolume", "volume"))
                .stream().map(CompanyDto::toDto)
                .collect(Collectors.toList());

        List<CompanyDto> other = repository
                .findCompaniesBySymbolNotInOrderBySymbolAsc(
                        topFive
                                .stream()
                                .map(CompanyDto::getSymbol)
                                .collect(Collectors.toList()))
                .stream()
                .map(CompanyDto::toDto)
                .collect(Collectors.toList());
        return CompletableFuture.completedFuture(
                Stream.of(topFive, other)
                        .flatMap(Collection::stream)
                        .collect(Collectors.toList()));
    }
}
