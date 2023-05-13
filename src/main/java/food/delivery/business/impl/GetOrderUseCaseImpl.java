package food.delivery.business.impl;

import food.delivery.business.GetOrderUseCase;
import food.delivery.business.exception.UnauthorizedDataAccessException;
import food.delivery.domain.AccessToken;
import food.delivery.domain.Order;
import food.delivery.persistence.OrderRepository;
import food.delivery.persistence.entity.OrderEntity;
import food.delivery.persistence.entity.RestaurantEntity;
import food.delivery.persistence.entity.RoleEnum;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class GetOrderUseCaseImpl implements GetOrderUseCase {
    private OrderRepository orderRepository;
    private AccessToken requestAccessToken;

    @Transactional
    @Override
    public Optional<Order> getOrder(Long orderId) {
        Optional<OrderEntity> optionalOrder = orderRepository.findById(orderId);
        if(optionalOrder.isPresent()){
            OrderEntity order = optionalOrder.get();
            RestaurantEntity restaurant = order.getRestaurant();

            if(!requestAccessToken.hasRole(RoleEnum.ADMIN.name())){
                if(!Objects.equals(requestAccessToken.getUserId(), restaurant.getUser().getId())){
                    throw new UnauthorizedDataAccessException("USER_ID_NOT_FROM_LOGGED_IN_USER");
                }
            }
        }
        return orderRepository.findById(orderId).map(OrderConverter::convert);
    }
}
