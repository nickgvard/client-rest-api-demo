package my.education.iexcloudapidemo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

/**
 * @author Nikita Gvardeev
 * 17.01.2022
 */

@Document
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Stock {

    @MongoId
    private Long id;
    private Long volume;
    private Float latestPrice;

}
