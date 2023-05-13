package food.delivery.business.impl;

import food.delivery.business.DeleteOrderUseCase;
import food.delivery.business.exception.UnauthorizedDataAccessException;
import food.delivery.domain.AccessToken;
import food.delivery.persistence.OrderRepository;
import food.delivery.persistence.entity.OrderEntity;
import food.delivery.persistence.entity.RoleEnum;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DeleteOrderUseCaseImpl implements DeleteOrderUseCase {
    private OrderRepository orderRepository;
    private AccessToken requestAccessToken;

    @Transactional
    @Override
    public void deleteOrder(Long orderId) {
        Optional<OrderEntity> optionalOrder = orderRepository.findById(orderId);
        if(optionalOrder.isPresent()){
            OrderEntity order = optionalOrder.get();

            if(!requestAccessToken.hasRole(RoleEnum.ADMIN.name())){
                if(!Objects.equals(requestAccessToken.getUserId(), order.getRestaurant().getUser().getId())){
                    throw new UnauthorizedDataAccessException("USER_ID_NOT_FROM_LOGGED_IN_USER");
                }
            }
        }

        this.orderRepository.deleteById(orderId);
    }
}
