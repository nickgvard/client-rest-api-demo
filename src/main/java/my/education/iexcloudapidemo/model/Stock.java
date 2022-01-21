package my.education.iexcloudapidemo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

/**
 * @author Nikita Gvardeev
 * 17.01.2022
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Stock {

    private Long previousVolume;
    private Long volume;
    private Float latestPrice;

}
