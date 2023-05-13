package food.delivery.business.impl;

import food.delivery.business.GetOrdersByUserIdUseCase;
import food.delivery.business.exception.UnauthorizedDataAccessException;
import food.delivery.domain.AccessToken;
import food.delivery.domain.GetAllOrdersByUserIdRequest;
import food.delivery.domain.GetOrdersResponse;
import food.delivery.domain.Order;
import food.delivery.persistence.OrderRepository;
import food.delivery.persistence.entity.OrderEntity;
import food.delivery.persistence.entity.RoleEnum;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class GetOrdersByUserIdUseCaseImpl implements GetOrdersByUserIdUseCase {
    private OrderRepository orderRepository;
    private AccessToken requestAccessToken;

    @Override
    public GetOrdersResponse getOrdersByUserId(GetAllOrdersByUserIdRequest request) {
        if(!requestAccessToken.hasRole(RoleEnum.ADMIN.name())){
            if(!Objects.equals(requestAccessToken.getUserId(), request.getUser_id())){
                throw new UnauthorizedDataAccessException("USER_ID_NOT_FROM_LOGGED_IN_USER");
            }
        }

        List<Order> orders;
        if(request.getUser_id() != null){
            orders = orderRepository.findAllByUser_Id(request.getUser_id()).stream().map(OrderConverter::convert).toList();
        }
        else {
            orders = orderRepository.findAll().stream().map(OrderConverter::convert).toList();
        }

        return GetOrdersResponse.builder().orders(orders).build();
    }
}
