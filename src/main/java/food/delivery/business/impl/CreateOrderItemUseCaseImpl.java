package food.delivery.business.impl;

import food.delivery.business.CreateOrderItemUseCase;
import food.delivery.business.exception.UnauthorizedDataAccessException;
import food.delivery.domain.AccessToken;
import food.delivery.domain.CreateOrderItemRequest;
import food.delivery.domain.CreateOrderItemResponse;
import food.delivery.persistence.ItemRepository;
import food.delivery.persistence.OrderItemRepository;
import food.delivery.persistence.OrderRepository;
import food.delivery.persistence.RestaurantRepository;
import food.delivery.persistence.entity.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CreateOrderItemUseCaseImpl implements CreateOrderItemUseCase {
    private OrderItemRepository orderItemRepository;
    private OrderRepository orderRepository;
    private ItemRepository itemRepository;
    private AccessToken requestAccessToken;

    @Transactional
    @Override
    public CreateOrderItemResponse createOrderItem(CreateOrderItemRequest request){
        OrderItemEntity savedOrderItem = saveNewOrderItem(request);
        assert savedOrderItem != null;
        return CreateOrderItemResponse.builder().orderItemId(savedOrderItem.getId()).build();
    }

    private OrderItemEntity saveNewOrderItem(CreateOrderItemRequest request){
        Optional<OrderEntity> orderOptional = orderRepository.findById(request.getOrder_id());
        Optional<ItemEntity> itemOptional = itemRepository.findById(request.getItem_id());

        if(orderOptional.isPresent() && itemOptional.isPresent()){
            OrderEntity order = orderOptional.get();
            ItemEntity item = itemOptional.get();

            if(!requestAccessToken.hasRole(RoleEnum.ADMIN.name())){
                if(!Objects.equals(requestAccessToken.getUserId(), order.getUser().getId())){
                    throw new UnauthorizedDataAccessException("USER_ID_NOT_FROM_LOGGED_IN_USER");
                }
            }

//            if(!requestAccessToken.hasRole(RoleEnum.ADMIN.name())){
//                if(requestAccessToken.getUserId() != order.getRestaurant().getUser().getId()){
//                    throw new UnauthorizedDataAccessException("USER_ID_NOT_FROM_LOGGED_IN_USER");
//                }
//            }

            OrderItemEntity newOrderItem = OrderItemEntity.builder().order(order).item(item).build();
            return orderItemRepository.save(newOrderItem);
        }
        return null;
    }
}
