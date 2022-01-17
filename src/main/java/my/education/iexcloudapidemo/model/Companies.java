package my.education.iexcloudapidemo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author Nikita Gvardeev
 * 17.01.2022
 */

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Companies implements Serializable {

    private List<Company> items;
}
