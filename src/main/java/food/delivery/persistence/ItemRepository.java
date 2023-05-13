package food.delivery.persistence;

import food.delivery.persistence.entity.ItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemRepository extends JpaRepository<ItemEntity, Long> {
    String findByName(String name);
    List<ItemEntity> findAllByRestaurant_IdOrderByIdAsc(Long restaurant_id);

    @Query(value = "SELECT * FROM items WHERE items.restaurant_id IN " +
            "(SELECT items.restaurant_id FROM restaurants WHERE items.restaurant_id = restaurants.id AND restaurants.id IN " +
            "(SELECT restaurants.id FROM orders WHERE restaurants.id = orders.restaurant_id AND orders.user_id = ?1))", nativeQuery = true)
    List<ItemEntity> getAllItemsIfUserOrdered(Long userId);
}
