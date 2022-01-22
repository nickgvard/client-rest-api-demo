package my.education.iexcloudapidemo.service.impl;

import lombok.RequiredArgsConstructor;
import my.education.iexcloudapidemo.dto.CompanyDto;
import my.education.iexcloudapidemo.dto.StockDto;
import my.education.iexcloudapidemo.model.Company;
import my.education.iexcloudapidemo.model.Stock;
import my.education.iexcloudapidemo.repository.MongoCompanyRepository;
import my.education.iexcloudapidemo.service.CompanyService;
import my.education.iexcloudapidemo.service.StockService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
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

    @Value("${iexcloud.companies}")
    private String urlApi;
    private final RestTemplate restTemplate;
    private final MongoCompanyRepository repository;
    private final StockService stockService;

    @Async("apiExecutor")
    @Override
    public CompletableFuture<List<CompanyDto>> findAllFromApi() {
        Supplier<List<CompanyDto>> findAll = () -> {
            ResponseEntity<List<Company>> responseEntity = restTemplate
                    .exchange(
                            urlApi,
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
    public CompletableFuture<CompanyDto> save(CompanyDto companyDto) {
        StockDto bySymbol = stockService.findBySymbol(companyDto.getSymbol());
        companyDto.setStockDto(bySymbol);

        Company company = CompanyDto.toDocument(companyDto);
        Company saved = repository.save(company);
        return CompletableFuture.completedFuture(CompanyDto.toDto(saved));
    }

    @Override
    public CompletableFuture<List<CompanyDto>> topFiveAndOther() {
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
