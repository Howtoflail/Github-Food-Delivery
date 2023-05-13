package food.delivery.business.impl;

import food.delivery.business.GetRestaurantsIfUserOrderedUseCase;
import food.delivery.business.exception.UnauthorizedDataAccessException;
import food.delivery.domain.*;
import food.delivery.persistence.RestaurantRepository;
import food.delivery.persistence.entity.RoleEnum;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class GetRestaurantsIfUserOrderedUseCaseImpl implements GetRestaurantsIfUserOrderedUseCase {
    private RestaurantRepository restaurantRepository;
    private AccessToken requestAccessToken;

    @Override
    public GetRestaurantsResponse getRestaurantsIfUserOrdered(GetAllRestaurantsIfUserOrderedRequest request) {
        if(!requestAccessToken.hasRole(RoleEnum.ADMIN.name())){
            if(!Objects.equals(requestAccessToken.getUserId(), request.getUser_id())){
                throw new UnauthorizedDataAccessException("USER_ID_NOT_FROM_LOGGED_IN_USER");
            }
        }

        List<Restaurant> restaurants;
        if(request.getUser_id() != null){
            restaurants = restaurantRepository.getRestaurantIfUserOrdered(request.getUser_id()).stream()
                    .map(RestaurantConverter::convert).toList();
        }
        else{
            restaurants = restaurantRepository.findAll().stream().map(RestaurantConverter::convert).toList();
        }

        return GetRestaurantsResponse.builder().restaurants(restaurants).build();
    }
}
