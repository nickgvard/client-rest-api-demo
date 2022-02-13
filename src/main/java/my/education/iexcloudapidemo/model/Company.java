package my.education.iexcloudapidemo.model;

import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;

/**
 * @author Nikita Gvardeev
 * 17.01.2022
 */

@Entity
@Table(name = "companies")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String symbol;
    @Column(name = "company_name")
    private String companyName;
    @Type(type = "org.hibernate.type.NumericBooleanType")
    @Column(name = "is_enabled")
    private Boolean isEnabled;
    @Column(name = "previous_volume")
    private Long previousVolume;
    private Long volume;
    @Column(name = "latest_price")
    private Float latestPrice;
}
