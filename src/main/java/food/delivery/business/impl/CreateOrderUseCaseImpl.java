package food.delivery.business.impl;

import food.delivery.business.CreateOrderUseCase;
import food.delivery.business.exception.UnauthorizedDataAccessException;
import food.delivery.domain.AccessToken;
import food.delivery.domain.CreateOrderRequest;
import food.delivery.domain.CreateOrderResponse;
import food.delivery.persistence.OrderRepository;
import food.delivery.persistence.RestaurantRepository;
import food.delivery.persistence.UserRepository;
import food.delivery.persistence.entity.OrderEntity;
import food.delivery.persistence.entity.RestaurantEntity;
import food.delivery.persistence.entity.RoleEnum;
import food.delivery.persistence.entity.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CreateOrderUseCaseImpl implements CreateOrderUseCase {
    private OrderRepository orderRepository;
    private UserRepository userRepository;
    private RestaurantRepository restaurantRepository;
    private AccessToken requestAccessToken;

    @Transactional
    @Override
    public CreateOrderResponse createOrder(CreateOrderRequest request) {
        OrderEntity savedOrder = saveNewOrder(request);
        assert savedOrder != null;
        return CreateOrderResponse.builder().orderId(savedOrder.getId()).build();
    }

    private OrderEntity saveNewOrder(CreateOrderRequest request){
        Optional<UserEntity> userOptional = userRepository.findById(request.getUser_id());
        Optional<RestaurantEntity> restaurantOptional = restaurantRepository.findById(request.getRestaurant_id());

        if(userOptional.isPresent() && restaurantOptional.isPresent()){
            UserEntity user = userOptional.get();
            RestaurantEntity restaurant = restaurantOptional.get();

            //preventing users to create orders on other users accounts
            if(!requestAccessToken.hasRole(RoleEnum.ADMIN.name())){
                if(!Objects.equals(requestAccessToken.getUserId(), user.getId())){
                    throw new UnauthorizedDataAccessException("USER_ID_NOT_FROM_LOGGED_IN_USER");
                }
            }

            ZoneId zoneId = ZoneId.of("UTC");
            OffsetDateTime dateTime = OffsetDateTime.now(zoneId);
            OrderEntity newOrder = OrderEntity.builder().user(user).restaurant(restaurant).dateTime(dateTime).build();
            return orderRepository.save(newOrder);
        }
        return null;
    }
}
