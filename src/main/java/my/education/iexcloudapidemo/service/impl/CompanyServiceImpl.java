package my.education.iexcloudapidemo.service.impl;

import com.mongodb.client.result.UpdateResult;
import lombok.RequiredArgsConstructor;
import my.education.iexcloudapidemo.dto.CompanyDto;
import my.education.iexcloudapidemo.model.Company;
import my.education.iexcloudapidemo.repository.MongoCompanyRepository;
import my.education.iexcloudapidemo.service.CompanyService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
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
    private final MongoTemplate mongoTemplate;

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

    @Override
    public CompletableFuture<CompanyDto> save(CompanyDto companyDto) {
        Query query = new Query();
        query.addCriteria(Criteria.where("previousVolume").ne(companyDto.getStockDto().getPreviousVolume()));

        Update upsert = new Update();
        upsert.set("previousVolume", companyDto.getStockDto().getPreviousVolume());

        UpdateResult result = mongoTemplate.upsert(query, upsert, Company.class);

        if (Objects.isNull(result.getUpsertedId()))
            throw new NullPointerException("Upsert with: " + companyDto.getSymbol() + " is failed");

        String id = result.getUpsertedId().asString().getValue();

        Company company = mongoTemplate.findById(id, Company.class);

        return CompletableFuture.completedFuture(CompanyDto.toDto(company));
    }
}
