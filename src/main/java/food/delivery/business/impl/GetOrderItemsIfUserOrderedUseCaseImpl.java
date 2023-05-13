package food.delivery.business.impl;

import food.delivery.business.GetOrderItemsIfUserOrderedUseCase;
import food.delivery.business.exception.UnauthorizedDataAccessException;
import food.delivery.domain.AccessToken;
import food.delivery.domain.GetAllOrderItemsIfUserOrderedRequest;
import food.delivery.domain.GetOrderItemsResponse;
import food.delivery.domain.OrderItem;
import food.delivery.persistence.OrderItemRepository;
import food.delivery.persistence.entity.RoleEnum;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class GetOrderItemsIfUserOrderedUseCaseImpl implements GetOrderItemsIfUserOrderedUseCase {
    private OrderItemRepository orderItemRepository;
    private AccessToken requestAccessToken;

    @Override
    public GetOrderItemsResponse getOrderItemsIfUserOrdered(GetAllOrderItemsIfUserOrderedRequest request) {
        if(!requestAccessToken.hasRole(RoleEnum.ADMIN.name())){
            if(!Objects.equals(requestAccessToken.getUserId(), request.getUser_id())){
                throw new UnauthorizedDataAccessException("USER_ID_NOT_FROM_LOGGED_IN_USER");
            }
        }

        List<OrderItem> orderItems;
        if(request.getUser_id() != null){
            orderItems = orderItemRepository.getOrderItemsIfUserOrdered(request.getUser_id()).stream().map(OrderItemConverter::convert).toList();
        }
        else{
            orderItems = orderItemRepository.findAll().stream().map(OrderItemConverter::convert).toList();
        }

        return GetOrderItemsResponse.builder().orderItems(orderItems).build();
    }
}
