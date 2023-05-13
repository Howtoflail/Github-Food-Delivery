package food.delivery.business.impl;

import food.delivery.business.GetOrderItemUseCase;
import food.delivery.business.exception.UnauthorizedDataAccessException;
import food.delivery.domain.AccessToken;
import food.delivery.domain.OrderItem;
import food.delivery.persistence.OrderItemRepository;
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
public class GetOrderItemUseCaseImpl implements GetOrderItemUseCase {
    private OrderItemRepository orderItemRepository;
    private RestaurantRepository restaurantRepository;
    private AccessToken requestAccessToken;

    @Transactional
    @Override
    public Optional<OrderItem> getOrderItem(Long orderItemId) {
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

        return orderItemRepository.findById(orderItemId).map(OrderItemConverter::convert);
    }
}
