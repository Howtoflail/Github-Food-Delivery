package food.delivery.business.impl;

import food.delivery.business.GetUsersIfOrderedUseCase;
import food.delivery.business.exception.UnauthorizedDataAccessException;
import food.delivery.domain.AccessToken;
import food.delivery.domain.GetUsersResponse;
import food.delivery.domain.User;
import food.delivery.persistence.RestaurantRepository;
import food.delivery.persistence.UserRepository;
import food.delivery.persistence.entity.RestaurantEntity;
import food.delivery.persistence.entity.RoleEnum;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class GetUsersIfOrderedUseCaseImpl implements GetUsersIfOrderedUseCase {
    private UserRepository userRepository;
    private RestaurantRepository restaurantRepository;
    private AccessToken requestAccessToken;

    @Transactional
    @Override
    public GetUsersResponse getUsersIfOrdered(Long restaurantId) {
        Optional<RestaurantEntity> optionalRestaurant = restaurantRepository.findById(restaurantId);
        if (optionalRestaurant.isPresent()) {
            RestaurantEntity restaurant = optionalRestaurant.get();

            if (!requestAccessToken.hasRole(RoleEnum.ADMIN.name())) {
                if (!Objects.equals(requestAccessToken.getUserId(), restaurant.getUser().getId())) {
                    throw new UnauthorizedDataAccessException("USER_ID_NOT_FROM_LOGGED_IN_USER");
                }
            }

            List<User> users = userRepository.getUsersFromRestaurantIfOrdered(restaurantId)
                    .stream().map(UserConverter::convert).toList();

            if (users.isEmpty()) {
                return null;
            }
            return GetUsersResponse.builder().users(users).build();
        }
//            Optional<UserEntity> optionalUser = userRepository.getUserDetailsFromRestaurantIfOrdered(restaurantId, userId);
//            if(optionalUser.isPresent()){
//                return  optionalUser.map(UserConverter::convert);
//            }
//        }
            return null;
    }
}
