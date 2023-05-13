package food.delivery.business.impl;

import food.delivery.business.exception.UnauthorizedDataAccessException;
import food.delivery.persistence.RestaurantRepository;
import food.delivery.persistence.entity.RestaurantEntity;
import food.delivery.persistence.entity.RoleEnum;
import org.springframework.util.NumberUtils;
import org.springframework.util.StringUtils;
import food.delivery.business.GetOrdersUseCase;
import food.delivery.domain.AccessToken;
import food.delivery.domain.GetAllOrdersRequest;
import food.delivery.domain.GetOrdersResponse;
import food.delivery.domain.Order;
import food.delivery.persistence.OrderRepository;
import food.delivery.persistence.entity.OrderEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class GetOrdersUseCaseImpl implements GetOrdersUseCase {
    private OrderRepository orderRepository;
    private RestaurantRepository restaurantRepository;
    private AccessToken requestAccessToken;

    @Transactional
    @Override
    public GetOrdersResponse getOrders(final GetAllOrdersRequest request) {
        Optional<RestaurantEntity> optionalRestaurant = restaurantRepository.findById(request.getRestaurant_id());
        if(optionalRestaurant.isPresent()){
            RestaurantEntity restaurant = optionalRestaurant.get();

            if(!requestAccessToken.hasRole(RoleEnum.ADMIN.name())){
                if(!Objects.equals(requestAccessToken.getUserId(), restaurant.getUser().getId())){
                    throw new UnauthorizedDataAccessException("USER_ID_NOT_FROM_LOGGED_IN_USER");
                }
            }
        }

        List<Order> results;
        if(request.getRestaurant_id() != null) {
            results = orderRepository.findAllByRestaurant_IdOrderByIdAsc(request.getRestaurant_id()).stream()
                    .map(OrderConverter::convert).toList();
        }
        else {
            results = orderRepository.findAll().stream()
                    .map(OrderConverter::convert).toList();
        }

        return GetOrdersResponse.builder().orders(results).build();
    }
}
