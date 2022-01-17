package my.education.iexcloudapidemo.restclient;

import lombok.RequiredArgsConstructor;
import my.education.iexcloudapidemo.model.Company;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author Nikita Gvardeev
 * 17.01.2022
 */

@Service
@RequiredArgsConstructor
public class GetAllCompanyRestClientCommand implements RestClientCommand<List<Company>> {

    @Value("${iexcloud.companies}")
    private String urlApi;
    private final RestTemplate restTemplate;

    @Async
    @Override
    public CompletableFuture<List<Company>> executeAsync() {
        ResponseEntity<Company[]> response = restTemplate.getForEntity(urlApi, Company[].class);
        return CompletableFuture.completedFuture(Arrays.asList(response.getBody()));
    }
}
