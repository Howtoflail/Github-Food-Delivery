package food.delivery.business;

import food.delivery.domain.CreateRestaurantRequest;
import food.delivery.domain.CreateRestaurantResponse;
import org.springframework.web.multipart.MultipartFile;

public interface CreateRestaurantUseCase {
    CreateRestaurantResponse createRestaurant(CreateRestaurantRequest request);
}
