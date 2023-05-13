package food.delivery.business.impl;

import food.delivery.business.GetOrderItemsUseCase;
import food.delivery.business.exception.UnauthorizedDataAccessException;
import food.delivery.domain.AccessToken;
import food.delivery.domain.GetAllOrderItemsRequest;
import food.delivery.domain.GetOrderItemsResponse;
import food.delivery.domain.OrderItem;
import food.delivery.persistence.OrderItemRepository;
import food.delivery.persistence.OrderRepository;
import food.delivery.persistence.RestaurantRepository;
import food.delivery.persistence.entity.OrderEntity;
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
public class GetOrderItemsUseCaseImpl implements GetOrderItemsUseCase {
    private OrderItemRepository orderItemRepository;
    private RestaurantRepository restaurantRepository;
    private AccessToken requestAccessToken;

    @Transactional
    @Override
    public GetOrderItemsResponse getOrderItems(GetAllOrderItemsRequest request) {
        Optional<RestaurantEntity> optionalRestaurant = restaurantRepository.findById(request.getRestaurant_id());
        if(optionalRestaurant.isPresent()){
            RestaurantEntity restaurant = optionalRestaurant.get();

            if(!requestAccessToken.hasRole(RoleEnum.ADMIN.name())){
                if(!Objects.equals(requestAccessToken.getUserId(), restaurant.getUser().getId())){
                    throw new UnauthorizedDataAccessException("USER_ID_NOT_FROM_LOGGED_IN_USER");
                }
            }
        }

        List<OrderItem> orderItems;
        if(request.getRestaurant_id() != null){
            orderItems = orderItemRepository.findAllByRestaurant_Id(request.getRestaurant_id()).stream()
                    .map(OrderItemConverter::convert).toList();
        }
        else {
            orderItems = orderItemRepository.findAll().stream().map(OrderItemConverter::convert).toList();
        }

        return GetOrderItemsResponse.builder().orderItems(orderItems).build();
    }
}
