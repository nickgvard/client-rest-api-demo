package my.education.iexcloudapidemo.repository;

import my.education.iexcloudapidemo.model.Logbook;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author Nikita Gvardeev
 * 22.01.2022
 */
public interface MongoLogbookRepository extends MongoRepository<Logbook, String> {
}