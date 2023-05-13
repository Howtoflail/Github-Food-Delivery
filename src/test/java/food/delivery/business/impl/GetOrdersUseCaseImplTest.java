package food.delivery.business.impl;

import food.delivery.domain.GetAllOrdersRequest;
import food.delivery.domain.GetOrdersResponse;
import food.delivery.domain.Order;
import food.delivery.persistence.OrderRepository;
import food.delivery.persistence.RestaurantRepository;
import food.delivery.persistence.entity.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class GetOrdersUseCaseImplTest {
    @Mock
    private OrderRepository orderRepositoryMock;
    @Mock
    private RestaurantRepository restaurantRepositoryMock;
    @InjectMocks
    private GetOrdersUseCaseImpl getOrdersUseCase;

    @Test
    void getOrders_shouldReturnAllOrdersConverted() {
        UserEntity firstUserEntity = UserEntity.builder()
                .id(1L)
                .first_name("Tom")
                .last_name("Rolland")
                .email("tomrolland@gmail.com")
                .address("Buna Vestire 54")
                .phone("+40736756631")
                .password("Marele123")
                .build();
        firstUserEntity.setUserRoles(Set.of(UserRoleEntity.builder().user(firstUserEntity).role(RoleEnum.USER).build()));

        UserEntity secondUserEntity = UserEntity.builder()
                .id(2L)
                .first_name("Alex")
                .last_name("Terrible")
                .email("alexterrible@gmail.com")
                .address("Cold Russia")
                .phone("+40736756631")
                .password("Bigterrible3")
                .build();
        secondUserEntity.setUserRoles(Set.of(UserRoleEntity.builder().user(secondUserEntity).role(RoleEnum.USER).build()));

        RestaurantEntity firstRestaurantEntity = RestaurantEntity.builder()
                .id(1L)
                .name("Bruh")
                .user(firstUserEntity)
                .address("Center")
                .type("american")
                .build();

        ZoneId zoneId = ZoneId.of("UTC");
        OffsetDateTime dateTime = OffsetDateTime.now(zoneId);

        OrderEntity firstOrderEntity = OrderEntity.builder()
                .id(1L)
                .user(firstUserEntity)
                .restaurant(firstRestaurantEntity)
                .dateTime(dateTime)
                .build();

        OrderEntity secondOrderEntity = OrderEntity.builder()
                .id(2L)
                .user(secondUserEntity)
                .restaurant(firstRestaurantEntity)
                .dateTime(dateTime)
                .build();

        when(orderRepositoryMock.findAllByRestaurant_IdOrderByIdAsc(firstRestaurantEntity.getId())).thenReturn(List.of(firstOrderEntity, secondOrderEntity));

        GetOrdersResponse actualResult = getOrdersUseCase.getOrders(GetAllOrdersRequest.builder().restaurant_id(firstRestaurantEntity.getId()).build());

        Order firstOrder = Order.builder()
                .id(1L)
                .user_id(firstUserEntity.getId())
                .restaurant_id(firstRestaurantEntity.getId())
                .dateTime(dateTime)
                .build();

        Order secondOrder = Order.builder()
                .id(2L)
                .user_id(secondUserEntity.getId())
                .restaurant_id(firstRestaurantEntity.getId())
                .dateTime(dateTime)
                .build();

        GetOrdersResponse expectedResult = GetOrdersResponse.builder()
                .orders(List.of(firstOrder, secondOrder))
                .build();
        assertEquals(expectedResult, actualResult);

        verify(orderRepositoryMock).findAllByRestaurant_IdOrderByIdAsc(firstRestaurantEntity.getId());
    }
}