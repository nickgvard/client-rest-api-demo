package my.education.iexcloudapidemo.repository;

import my.education.iexcloudapidemo.model.Logbook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Nikita Gvardeev
 * 22.01.2022
 */

@Repository
public interface LogbookRepository extends JpaRepository<Logbook, Long> {

    @Query("select l from Logbook l where abs(l.currentPrice - l.oldPrice) > 0 " +
            "order by abs(l.currentPrice - l.oldPrice) desc")
    List<Logbook> findTop5();
}