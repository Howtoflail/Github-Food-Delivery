package food.delivery.business.impl;

import food.delivery.business.GetRestaurantUseCase;
import food.delivery.business.exception.UnauthorizedDataAccessException;
import food.delivery.domain.AccessToken;
import food.delivery.domain.Restaurant;
import food.delivery.persistence.RestaurantRepository;
import food.delivery.persistence.entity.RestaurantEntity;
import food.delivery.persistence.entity.RoleEnum;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@AllArgsConstructor
public class GetRestaurantUseCaseImpl implements GetRestaurantUseCase {
    private RestaurantRepository restaurantRepository;
//    private AccessToken requestAccessToken;
//    @Transactional
    @Override
    public Optional<Restaurant> getRestaurant(Long restaurantId) {
//        Optional<RestaurantEntity> restaurantOptional = restaurantRepository.findById(restaurantId);
//        if(restaurantOptional.isPresent()){
//            RestaurantEntity restaurant = restaurantOptional.get();
//
//            if(!requestAccessToken.hasRole(RoleEnum.ADMIN.name())){
//                if(requestAccessToken.getUserId() != restaurant.getUser().getId()){
//                    throw new UnauthorizedDataAccessException("USER_ID_NOT_FROM_LOGGED_IN_USER");
//                }
//            }
//        }
        return restaurantRepository.findById(restaurantId).map(RestaurantConverter::convert);
    }
}
