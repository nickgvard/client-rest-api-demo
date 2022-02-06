package my.education.iexcloudapidemo.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.education.iexcloudapidemo.dto.CompanyDto;
import my.education.iexcloudapidemo.dto.LogbookDto;
import my.education.iexcloudapidemo.model.Logbook;
import my.education.iexcloudapidemo.repository.LogbookRepository;
import my.education.iexcloudapidemo.service.LogbookService;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @author Nikita Gvardeev
 * 22.01.2022
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class LogbookServiceImpl implements LogbookService {

    private final LogbookRepository repository;

    @Async
    @Override
    public CompletableFuture<List<LogbookDto>> saveAll(List<CompanyDto> companies) {
        return CompletableFuture.supplyAsync(() -> {
            List<Logbook> toSave = companies.stream()
                    .map(companyDto -> Logbook.builder()
                            .company(CompanyDto.toEntity(companyDto))
                            .currentPrice(companyDto.getLatestPrice())
                            .build())
                    .collect(Collectors.toList());

            List<Logbook> persists = toSave.stream().map(logbook -> {
                Logbook bySymbol = repository.findByCompanySymbol(logbook.getCompany().getSymbol());
                if (Objects.nonNull(bySymbol)) {
                    bySymbol.setOldPrice(bySymbol.getCurrentPrice());
                    bySymbol.setCurrentPrice(logbook.getCurrentPrice());
                    return repository.save(bySymbol);
                } else {
                    return repository.save(logbook);
                }
            }).collect(Collectors.toList());

            return persists
                    .stream()
                    .map(LogbookDto::toDto)
                    .collect(Collectors.toList());
        });
    }

    @Async
    @Override
    public CompletableFuture<List<LogbookDto>> findTop5ByDeltaLatestPrice() {
        List<Logbook> findByDelta = repository.findTop5By(PageRequest.of(0, 5));
        return CompletableFuture.completedFuture(findByDelta
                .stream()
                .map(LogbookDto::toDto)
                .collect(Collectors.toList()));
    }
}
