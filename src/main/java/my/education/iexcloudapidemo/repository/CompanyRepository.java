package my.education.iexcloudapidemo.repository;

import my.education.iexcloudapidemo.model.Company;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/**
 * @author Nikita Gvardeev
 * 17.01.2022
 */

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

    List<Company> findTop5By(Sort sort);

    List<Company> findCompaniesBySymbolNotInOrderBySymbolDesc(Collection<String> symbols);
}
