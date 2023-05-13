package food.delivery.business;

import food.delivery.domain.CreateUserRequest;
import food.delivery.domain.CreateUserResponse;

public interface CreateUserRestaurantUseCase {
    CreateUserResponse createUserRestaurant(CreateUserRequest request);
}
