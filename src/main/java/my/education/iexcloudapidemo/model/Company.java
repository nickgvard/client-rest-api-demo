package my.education.iexcloudapidemo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
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
public class Company {

    @MongoId
    private Long id;
    private String symbol;
    private Boolean isEnabled;
    private Stock stock;
}
