package my.education.iexcloudapidemo.service.impl;

import lombok.RequiredArgsConstructor;
import my.education.iexcloudapidemo.dto.LogbookDto;
import my.education.iexcloudapidemo.model.Logbook;
import my.education.iexcloudapidemo.repository.MongoLogbookRepository;
import my.education.iexcloudapidemo.service.LogbookService;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

/**
 * @author Nikita Gvardeev
 * 22.01.2022
 */

@Service
@RequiredArgsConstructor
public class LogbookServiceImpl implements LogbookService {

    private final MongoLogbookRepository repository;
    private final MongoTemplate mongoTemplate;

    @Override
    public CompletableFuture<LogbookDto> save(LogbookDto logbookDto) {
        Logbook persist = repository.findById(logbookDto.getId()).orElse(null);

        Logbook currentLogbook = LogbookDto.toDocument(logbookDto);

        if (Objects.nonNull(persist)) {
            currentLogbook.setOldPrice(persist.getCurrentPrice());
        }

        Logbook saved = repository.save(currentLogbook);
        return CompletableFuture.completedFuture(LogbookDto.toDto(saved));
    }

    @Override
    public CompletableFuture<List<LogbookDto>> topFiveDeltaLatestPrice() {
        ProjectionOperation projectionOperation = project("symbol")
                .and("priceCur")
                .minus("priceOld")
                .absoluteValue()
                .as("absoluteDelta");
        SortOperation sortOperation = sort(Sort.by(Sort.Direction.DESC));
        LimitOperation limitOperation = limit(5);

        Aggregation aggregation = newAggregation(projectionOperation, sortOperation, limitOperation);

        AggregationResults<Logbook> results = mongoTemplate.aggregate(aggregation, "logbook", Logbook.class);

        List<LogbookDto> mappedResults = results.getMappedResults()
                .stream()
                .map(LogbookDto::toDto)
                .collect(Collectors.toList());

        return CompletableFuture.completedFuture(mappedResults);
    }
}
