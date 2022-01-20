package my.education.iexcloudapidemo.repository;

import my.education.iexcloudapidemo.model.Company;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author Nikita Gvardeev
 * 17.01.2022
 */
public interface MongoCompanyRepository extends MongoRepository<Company, String> {

}
