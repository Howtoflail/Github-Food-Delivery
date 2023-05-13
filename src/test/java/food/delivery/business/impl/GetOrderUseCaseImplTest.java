package food.delivery.business.impl;

import food.delivery.domain.AccessToken;
import food.delivery.domain.Order;
import food.delivery.domain.User;
import food.delivery.persistence.OrderRepository;
import food.delivery.persistence.entity.*;
import org.aspectj.weaver.ast.Or;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class GetOrderUseCaseImplTest {
    @Mock
    private OrderRepository orderRepositoryMock;
    @Mock
    private AccessToken requestAccessToken;
    @InjectMocks
    private GetOrderUseCaseImpl getOrderUseCase;

    @Test
    void getOrder_shouldReturnOrderConverted() {
        UserEntity userEntity = UserEntity.builder()
                .id(1L)
                .first_name("Tom")
                .last_name("Rolland")
                .email("tomrolland@gmail.com")
                .address("Buna Vestire 54")
                .phone("+40736756631")
                .password("Marele123")
                .build();
        userEntity.setUserRoles(Set.of(UserRoleEntity.builder().user(userEntity).role(RoleEnum.USER).build()));

        RestaurantEntity restaurantEntity = RestaurantEntity.builder()
                .id(1L)
                .name("Bruh")
                .user(userEntity)
                .address("Center")
                .type("american")
                .build();

        ZoneId zoneId = ZoneId.of("UTC");
        OffsetDateTime dateTime = OffsetDateTime.now(zoneId);

        OrderEntity orderEntity = OrderEntity.builder()
                .id(1L)
                .user(userEntity)
                .restaurant(restaurantEntity)
                .dateTime(dateTime)
                .build();

        when(orderRepositoryMock.findById(orderEntity.getId())).thenReturn(Optional.of(orderEntity));
        when(requestAccessToken.getUserId()).thenReturn(userEntity.getId());

        Optional<Order> actualResult = getOrderUseCase.getOrder(orderEntity.getId());

        Order order = Order.builder()
                .id(1L)
                .user_id(1L)
                .restaurant_id(1L)
                .dateTime(dateTime)
                .build();

        Optional<Order> expectedResult = Optional.of(order);
        assertEquals(expectedResult, actualResult);

        verify(orderRepositoryMock, times(2)).findById(1L);
    }
}