package food.delivery.business.impl;

import food.delivery.business.GetRestaurantIdUseCase;
import food.delivery.business.exception.UnauthorizedDataAccessException;
import food.delivery.domain.AccessToken;
import food.delivery.persistence.UserRepository;
import food.delivery.persistence.entity.RoleEnum;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class GetRestaurantIdUseCaseImpl implements GetRestaurantIdUseCase {
    private UserRepository userRepository;
    private AccessToken requestAccessToken;

    @Override
    public Long getRestaurantId(Long userId) {
        Optional<Long> optionalRestaurantId = userRepository.findRestaurantIdByUserId(userId);
        if(optionalRestaurantId.isPresent()){
            Long restaurantId = optionalRestaurantId.get();

            if(!requestAccessToken.hasRole(RoleEnum.ADMIN.name())){
                if(!Objects.equals(requestAccessToken.getUserId(), userId)){
                    throw new UnauthorizedDataAccessException("USER_ID_NOT_FROM_LOGGED_IN_USER");
                }
            }

            return restaurantId;
        }
        return null;
    }
}
