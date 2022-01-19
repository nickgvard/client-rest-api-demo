package my.education.iexcloudapidemo.service.impl;

import lombok.RequiredArgsConstructor;
import my.education.iexcloudapidemo.dto.CompanyDto;
import my.education.iexcloudapidemo.model.Company;
import my.education.iexcloudapidemo.service.CompanyService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

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

    @Async("apiExecutor")
    @Override
    public CompletableFuture<List<CompanyDto>> findAll() {
        return CompletableFuture.supplyAsync(this::companies);
    }

    private List<CompanyDto> companies() {
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
    }
}
