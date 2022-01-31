package my.education.iexcloudapidemo.service.impl;

import lombok.RequiredArgsConstructor;
import my.education.iexcloudapidemo.dto.CompanyDto;
import my.education.iexcloudapidemo.dto.LogbookDto;
import my.education.iexcloudapidemo.model.Logbook;
import my.education.iexcloudapidemo.repository.LogbookRepository;
import my.education.iexcloudapidemo.service.LogbookService;
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
public class LogbookServiceImpl implements LogbookService {

    private final LogbookRepository repository;

    @Async("apiExecutor")
    @Override
    public CompletableFuture<List<LogbookDto>> saveAll(List<CompanyDto> companies) {
        List<LogbookDto> logbooks = companies
                .stream()
                .map(companyDto -> LogbookDto.builder()
                        .companyDto(companyDto)
                        .currentPrice(companyDto.getLatestPrice())
                        .build()).collect(Collectors.toList());

        for (LogbookDto dto : logbooks) {
            CompletableFuture<Logbook> findById = CompletableFuture
                    .supplyAsync(() -> repository.findById(dto.getId()).orElse(null));
            CompletableFuture<Logbook> toEntity = CompletableFuture
                    .supplyAsync(() -> LogbookDto.toEntity(dto));

            findById.thenCombine(toEntity, (found, entity) -> {
                if (Objects.nonNull(found)) {
                    entity.setOldPrice(found.getCurrentPrice());
                }
                return entity;
            });
        }

        List<Logbook> forSave = logbooks
                .stream()
                .map(LogbookDto::toEntity)
                .collect(Collectors.toList());

        List<Logbook> saved = repository.saveAll(forSave);

        return CompletableFuture.completedFuture(saved
                .stream()
                .map(LogbookDto::toDto)
                .collect(Collectors.toList()));
    }

    @Override
    public CompletableFuture<List<LogbookDto>> findTop5ByDeltaLatestPrice() {
        List<Logbook> findByDelta = repository.findTop5();
        return CompletableFuture.completedFuture(findByDelta
                .stream()
                .map(LogbookDto::toDto)
                .collect(Collectors.toList()));
    }
}
