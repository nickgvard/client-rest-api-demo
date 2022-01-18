package my.education.iexcloudapidemo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

/**
 * @author Nikita Gvardeev
 * 17.01.2022
 */

@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class Company {

    @MongoId
    private Long id;
    private String symbol;
    private Boolean isEnabled;
    private Stock stock;
}
