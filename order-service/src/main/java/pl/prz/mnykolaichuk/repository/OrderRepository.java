package pl.prz.mnykolaichuk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.prz.mnykolaichuk.model.entity.Order;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> getOrderListByUserId(String userId);
    Order getOrderById(Long orderId);

}
