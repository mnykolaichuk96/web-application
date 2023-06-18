package pl.prz.mnykolaichuk.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "order_table")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "orderNumber")
    private String orderNumber;
    @Column(length = 36)
    private String userId;
    @OneToMany(mappedBy = "order",
            cascade = CascadeType.ALL)
    //For create only 2 tables not three
    //https://vladmihalcea.com/the-best-way-to-map-a-onetomany-association-with-jpa-and-hibernate/
//    @JoinColumn(name = "order_id")
    private List<OrderLineItems> orderLineItemsList;
}
