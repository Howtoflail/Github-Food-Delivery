package food.delivery.business.impl;

import food.delivery.business.UpdateRestaurantUseCase;
import food.delivery.business.exception.UnauthorizedDataAccessException;
import food.delivery.domain.AccessToken;
import food.delivery.domain.UpdateRestaurantRequest;
import food.delivery.persistence.RestaurantRepository;
import food.delivery.persistence.entity.RestaurantEntity;
import food.delivery.persistence.entity.RoleEnum;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UpdateRestaurantUseCaseImpl implements UpdateRestaurantUseCase {
    private final RestaurantRepository restaurantRepository;
    private AccessToken requestAccessToken;

    @Transactional
    @Override
    public void updateRestaurant(UpdateRestaurantRequest request) {
        //get restaurant id from front end
        Optional<RestaurantEntity> restaurantOptional = restaurantRepository.findById(request.getId());

        if(restaurantOptional.isPresent()){
            RestaurantEntity restaurant = restaurantOptional.get();

            if(!requestAccessToken.hasRole(RoleEnum.ADMIN.name())){
                if(!Objects.equals(requestAccessToken.getUserId(), restaurant.getUser().getId())){
                    throw new UnauthorizedDataAccessException("USER_ID_NOT_FROM_LOGGED_IN_USER");
                }
            }

            updateFields(request, restaurant);
        }
    }

    private void updateFields(UpdateRestaurantRequest request, RestaurantEntity restaurant){
        restaurant.setId(request.getId());
        restaurant.setName(request.getName());
        restaurant.setType(request.getType());
        restaurant.setAddress(request.getAddress());

        restaurantRepository.save(restaurant);
    }
}
