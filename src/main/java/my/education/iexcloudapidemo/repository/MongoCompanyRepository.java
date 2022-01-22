package my.education.iexcloudapidemo.repository;

import my.education.iexcloudapidemo.model.Company;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Collection;
import java.util.List;

/**
 * @author Nikita Gvardeev
 * 17.01.2022
 */
public interface MongoCompanyRepository extends MongoRepository<Company, String>, QuerydslPredicateExecutor<Company> {

    List<Company> findTop5By(Sort sort);

    List<Company> findCompaniesBySymbolNotInOrderBySymbolDesc(Collection<String> symbols);
}
