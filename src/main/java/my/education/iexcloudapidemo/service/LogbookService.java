package my.education.iexcloudapidemo.service;

import my.education.iexcloudapidemo.dto.LogbookDto;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author Nikita Gvardeev
 * 22.01.2022
 */
public interface LogbookService {

    CompletableFuture<LogbookDto> save(LogbookDto logbookDto);

    CompletableFuture<List<LogbookDto>> topFiveDeltaLatestPrice();
}