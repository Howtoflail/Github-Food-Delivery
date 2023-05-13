package food.delivery.business.impl;

import food.delivery.business.DeleteOrderItemUseCase;
import food.delivery.business.exception.UnauthorizedDataAccessException;
import food.delivery.domain.AccessToken;
import food.delivery.persistence.OrderItemRepository;
import food.delivery.persistence.RestaurantRepository;
import food.delivery.persistence.entity.RestaurantEntity;
import food.delivery.persistence.entity.RoleEnum;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DeleteOrderItemUseCaseImpl implements DeleteOrderItemUseCase {
    private OrderItemRepository orderItemRepository;
    private RestaurantRepository restaurantRepository;
    private AccessToken requestAccessToken;

    @Override
    public void deleteOrderItem(Long orderItemId) {
        Long restaurantId = orderItemRepository.findRestaurantIdByOrderItemId(orderItemId);
        Optional<RestaurantEntity> optionalRestaurant = restaurantRepository.findById(restaurantId);
        if(optionalRestaurant.isPresent()){
            RestaurantEntity restaurant = optionalRestaurant.get();

            if(!requestAccessToken.hasRole(RoleEnum.ADMIN.name())){
                if(!Objects.equals(requestAccessToken.getUserId(), restaurant.getUser().getId())){
                    throw new UnauthorizedDataAccessException("USER_ID_NOT_FROM_LOGGED_IN_USER");
                }
            }
        }

        this.orderItemRepository.deleteById(orderItemId);
    }
}
