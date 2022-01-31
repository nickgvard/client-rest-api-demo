package my.education.iexcloudapidemo.service.impl;

import lombok.RequiredArgsConstructor;
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
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Nikita Gvardeev
 * 17.01.2022
 */

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    @Value("${iexcloud.url.companies}")
    private String apiCompanies;

    @Value("${iexcloud.url.stock}")
    private String urlStockApi;

    @Value("${iexcloud.stock.code}")
    private String stockKeyCode;

    private final RestTemplate restTemplate;
    private final CompanyRepository repository;

    @Async("apiExecutor")
    @Override
    public CompletableFuture<List<CompanyDto>> findAllFromApi() {
        Supplier<List<CompanyDto>> findAll = () -> {
            ResponseEntity<List<Company>> responseEntity = restTemplate
                    .exchange(
                            apiCompanies,
                            HttpMethod.GET,
                            null,
                            new ParameterizedTypeReference<>() {
                            });

            List<Company> companies = responseEntity.getBody();

            if (Objects.isNull(companies))
                throw new RuntimeException("API is invalid");

            return companies
                    .stream()
                    .map(CompanyDto::toDto)
                    .collect(Collectors.toList());
        };

        return CompletableFuture.supplyAsync(findAll);
    }

    @Async("apiExecutor")
    @Override
    public CompletableFuture<CompanyDto> findStockByCompanyFromApi(CompanyDto companyDto) {
        Map<String, String> param = new HashMap<>();
        param.put(stockKeyCode, companyDto.getSymbol());

        CompanyDto fromApi = restTemplate.getForObject(urlStockApi, CompanyDto.class, param);

        if (Objects.isNull(fromApi))
            throw new RuntimeException("API is invalid");

        companyDto.setPreviousVolume(fromApi.getPreviousVolume());
        companyDto.setVolume(fromApi.getVolume());
        companyDto.setLatestPrice(fromApi.getLatestPrice());

        return CompletableFuture.completedFuture(companyDto);
    }

    @Async("apiExecutor")
    @Override
    public CompletableFuture<List<CompanyDto>> saveAll(List<CompanyDto> companies) {
        List<Company> forSave = companies
                .stream()
                .map(CompanyDto::toEntity)
                .collect(Collectors.toList());

        List<Company> persists = repository.saveAll(forSave);

        return CompletableFuture.completedFuture(persists
                .stream()
                .map(CompanyDto::toDto)
                .collect(Collectors.toList()));
    }

    @Override
    public CompletableFuture<List<CompanyDto>> findTop5CompaniesAndOther() {
        List<CompanyDto> topFive = repository
                .findTop5By(
                        Sort.by(Sort.Direction.DESC, "previousVolume", "volume"))
                .stream().map(CompanyDto::toDto)
                .collect(Collectors.toList());

        List<CompanyDto> other = repository
                .findCompaniesBySymbolNotInOrderBySymbolDesc(
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
