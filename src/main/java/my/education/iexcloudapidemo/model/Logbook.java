package my.education.iexcloudapidemo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

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
    private LocalDate registry;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "company_id", referencedColumnName = "id")
    private Company company;
}
