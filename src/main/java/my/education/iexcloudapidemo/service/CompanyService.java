package my.education.iexcloudapidemo.service;

import my.education.iexcloudapidemo.dto.CompanyDto;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author Nikita Gvardeev
 * 19.01.2022
 */
public interface CompanyService {

    CompletableFuture<List<CompanyDto>> findAll();
}
