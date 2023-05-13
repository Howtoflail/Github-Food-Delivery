package food.delivery.business.impl;

import food.delivery.domain.GetAllItemsRequest;
import food.delivery.domain.GetItemsResponse;
import food.delivery.domain.Item;
import food.delivery.persistence.ItemRepository;
import food.delivery.persistence.UserRepository;
import food.delivery.persistence.entity.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class GetItemsUseCaseImplTest {
    @Mock
    private ItemRepository itemRepositoryMock;
    @InjectMocks
    private GetItemsUseCaseImpl getItemsUseCase;

    @Test
    void getItems_shouldReturnItemsConverted() {
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
                .name("Munch")
                .user(userEntity)
                .address("Center")
                .type("japanese")
                .build();

        ItemEntity firstItemEntity = ItemEntity.builder()
                .id(1L)
                .name("Bacon")
                .price(2.3)
                .restaurant(restaurantEntity)
                .build();

        ItemEntity secondItemEntity = ItemEntity.builder()
                .id(2L)
                .name("Cheese")
                .price(3.0)
                .restaurant(restaurantEntity)
                .build();

        when(itemRepositoryMock.findAllByRestaurant_IdOrderByIdAsc(restaurantEntity.getId())).thenReturn(List.of(firstItemEntity, secondItemEntity));

        GetItemsResponse actualResult = getItemsUseCase.getItems(GetAllItemsRequest.builder().restaurant_id(restaurantEntity.getId()).build());

        Item firstItem = Item.builder()
                .id(1L)
                .name("Bacon")
                .price(2.3)
                .restaurant_id(restaurantEntity.getId())
                .build();

        Item secondItem = Item.builder()
                .id(2L)
                .name("Cheese")
                .price(3.0)
                .restaurant_id(restaurantEntity.getId())
                .build();

        GetItemsResponse expectedResult = GetItemsResponse.builder()
                .items(List.of(firstItem, secondItem))
                .build();
        assertEquals(expectedResult, actualResult);

        verify(itemRepositoryMock).findAllByRestaurant_IdOrderByIdAsc(restaurantEntity.getId());
    }
}