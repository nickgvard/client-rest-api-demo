package my.education.iexcloudapidemo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Nikita Gvardeev
 * 17.01.2022
 */

@Data
@Builder
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Stock {

    private Long previousVolume;
    private Long volume;
    private Float latestPrice;

}
