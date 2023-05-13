package food.delivery.persistence;

import food.delivery.persistence.entity.RestaurantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RestaurantRepository extends JpaRepository<RestaurantEntity, Long> {
    String findByName(String name);

    @Query(value = "SELECT * FROM restaurants WHERE restaurants.id IN " +
            "(SELECT restaurants.id FROM orders WHERE restaurants.id = orders.restaurant_id AND orders.user_id = ?1)", nativeQuery = true)
    List<RestaurantEntity> getRestaurantIfUserOrdered(Long userId);
}
