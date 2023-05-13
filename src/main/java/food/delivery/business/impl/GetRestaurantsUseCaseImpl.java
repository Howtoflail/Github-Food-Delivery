package food.delivery.business.impl;

import food.delivery.business.GetRestaurantsUseCase;
import food.delivery.domain.AccessToken;
import food.delivery.domain.GetRestaurantsResponse;
import food.delivery.domain.Restaurant;
import food.delivery.persistence.RestaurantRepository;
import food.delivery.persistence.entity.RoleEnum;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GetRestaurantsUseCaseImpl implements GetRestaurantsUseCase {
    private RestaurantRepository restaurantRepository;

    @Override
    public GetRestaurantsResponse getRestaurants() {
        List<Restaurant> restaurants = restaurantRepository.findAll().stream().map(RestaurantConverter::convert).toList();

        return GetRestaurantsResponse.builder().restaurants(restaurants).build();
    }
}
