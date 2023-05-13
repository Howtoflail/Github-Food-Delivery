package food.delivery.business.impl;

import food.delivery.domain.AccessToken;
import food.delivery.domain.GetAllOrderItemsRequest;
import food.delivery.domain.GetOrderItemsResponse;
import food.delivery.domain.OrderItem;
import food.delivery.persistence.OrderItemRepository;
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
class GetOrderItemsUseCaseImplTest {
    @Mock
    private OrderItemRepository orderItemRepositoryMock;
    @Mock
    private RestaurantRepository restaurantRepositoryMock;
    @Mock
    private AccessToken requestAccessToken;
    @InjectMocks
    private GetOrderItemsUseCaseImpl getOrderItemsUseCase;
    @Test
    void getOrderItems_shouldReturnOrderItemsConverted() {
        UserEntity userEntity = UserEntity.builder()
                .id(1L)
                .first_name("Tom")
                .last_name("Rolland")
                .email("tomrolland@gmail.com")
                .address("Buna Vestire 54")
                .phone("+40736756631")
                .password("Marele123")
                .build();
        userEntity.setUserRoles(Set.of(UserRoleEntity.builder().user(userEntity).role(RoleEnum.RESTAURANT).build()));

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

        ItemEntity itemEntity = ItemEntity.builder()
                .id(1L)
                .name("Burger")
                .price(12.3)
                .restaurant(restaurantEntity)
                .build();

        OrderItemEntity firstOrderItemEntity = OrderItemEntity.builder()
                .id(1L)
                .item(itemEntity)
                .order(orderEntity)
                .build();

        OrderItemEntity secondOrderItemEntity = OrderItemEntity.builder()
                .id(2L)
                .item(itemEntity)
                .order(orderEntity)
                .build();

        when(orderItemRepositoryMock.findAllByRestaurant_Id(restaurantEntity.getId())).thenReturn(List.of(firstOrderItemEntity, secondOrderItemEntity));

        GetOrderItemsResponse actualResult = getOrderItemsUseCase.getOrderItems(GetAllOrderItemsRequest.builder().restaurant_id(restaurantEntity.getId()).build());

        OrderItem firstOrderItem = OrderItem.builder()
                .id(1L)
                .item_id(itemEntity.getId())
                .order_id(orderEntity.getId())
                .build();

        OrderItem secondOrderItem = OrderItem.builder()
                .id(2L)
                .item_id(itemEntity.getId())
                .order_id(orderEntity.getId())
                .build();

        GetOrderItemsResponse expectedResult = GetOrderItemsResponse.builder()
                .orderItems(List.of(firstOrderItem, secondOrderItem))
                .build();
        assertEquals(expectedResult, actualResult);

        verify(orderItemRepositoryMock).findAllByRestaurant_Id(restaurantEntity.getId());
    }
}