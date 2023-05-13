package food.delivery.persistence;

import food.delivery.persistence.entity.OrderItemEntity;
import food.delivery.persistence.entity.RestaurantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrderItemRepository extends JpaRepository<OrderItemEntity, Long> {

    @Query(value = "SELECT * FROM order_items WHERE order_items.order_id IN " +
            "(SELECT order_items.order_id FROM orders WHERE restaurant_id = ?1) ", nativeQuery = true)
    List<OrderItemEntity> findAllByRestaurant_Id(Long restaurant_id);

    @Query(value = "SELECT (SELECT restaurant_id FROM orders WHERE order_items.order_id=orders.id) FROM order_items " +
            "WHERE id = ?1", nativeQuery = true)
    Long findRestaurantIdByOrderItemId(Long id);

    @Query(value = "SELECT * FROM order_items WHERE order_items.order_id IN " +
            "(SELECT order_items.order_id FROM orders WHERE order_items.order_id = orders.id AND orders.user_id = ?1)", nativeQuery = true)
    List<OrderItemEntity> getOrderItemsIfUserOrdered(Long userId);
}