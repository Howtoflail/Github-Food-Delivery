package food.delivery.business;

import java.util.Optional;
import food.delivery.domain.Restaurant;

public interface GetRestaurantUseCase {
    Optional<Restaurant> getRestaurant(Long restaurantId);
}
