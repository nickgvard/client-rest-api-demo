package my.education.iexcloudapidemo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

/**
 * @author Nikita Gvardeev
 * 22.01.2022
 */

@Document("logbook")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Logbook {

    @Id
    private String id;
    @Indexed(unique = true)
    private String symbol;
    private Float oldPrice;
    private Float currentPrice;
    private LocalDate registry;
}
