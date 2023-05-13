package food.delivery.business;

import food.delivery.domain.UpdateRestaurantRequest;

public interface UpdateRestaurantUseCase {
    void updateRestaurant(UpdateRestaurantRequest request);
}
