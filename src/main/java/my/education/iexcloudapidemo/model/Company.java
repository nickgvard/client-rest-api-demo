package my.education.iexcloudapidemo.model;

import lombok.*;

import javax.persistence.*;

/**
 * @author Nikita Gvardeev
 * 17.01.2022
 */

@Entity
@Table(name = "companies", schema = "public")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String symbol;
    @Column(name = "is_enabled")
    private Boolean isEnabled;
    @Column(name = "previous_volume")
    private Long previousVolume;
    private Long volume;
    @Column(name = "latest_price")
    private Float latestPrice;

    @OneToOne(cascade = CascadeType.ALL)
    private Logbook logbook;
}
