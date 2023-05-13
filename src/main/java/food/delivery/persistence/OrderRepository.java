package food.delivery.persistence;

import food.delivery.persistence.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<OrderEntity, Long>{
    List<OrderEntity> findAllByRestaurant_IdOrderByIdAsc(Long restaurant_id);

    //@Query(value = "SELECT * FROM orders WHERE orders.user_id = ?1", nativeQuery = true)
    List<OrderEntity> findAllByUser_Id(Long userId);
}
