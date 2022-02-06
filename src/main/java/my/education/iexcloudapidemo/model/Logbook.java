package my.education.iexcloudapidemo.model;

import lombok.*;

import javax.persistence.*;

/**
 * @author Nikita Gvardeev
 * 22.01.2022
 */

@Entity
@Table(name = "logbook", schema = "public")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Logbook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "old_price")
    private Float oldPrice;

    @Column(name = "current_price")
    private Float currentPrice;

    @OneToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", referencedColumnName = "id", updatable = false)
    private Company company;
}
