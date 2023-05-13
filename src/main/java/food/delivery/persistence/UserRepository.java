package food.delivery.persistence;

import food.delivery.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    String findByEmail(String email);
    UserEntity queryByEmail(String email);
    @Query(value = "SELECT (SELECT id FROM restaurants WHERE users.id = restaurants.user_id) FROM users WHERE id = ?1", nativeQuery = true)
    Optional<Long> findRestaurantIdByUserId(Long userId);

    @Query(value = "SELECT * FROM users WHERE users.id IN " +
            "(SELECT users.id FROM orders WHERE orders.restaurant_id = ?1 AND users.id = orders.user_id)", nativeQuery = true)
    List<UserEntity> getUsersFromRestaurantIfOrdered(Long restaurantId);

    //    UserEntity queryById(Long id);
    //    Optional<UserEntity> findById(long userId);
}
