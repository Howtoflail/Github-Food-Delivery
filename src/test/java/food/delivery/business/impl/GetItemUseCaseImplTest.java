package food.delivery.business.impl;

import food.delivery.domain.AccessToken;
import food.delivery.domain.Item;
import food.delivery.persistence.ItemRepository;
import food.delivery.persistence.RestaurantRepository;
import food.delivery.persistence.entity.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class GetItemUseCaseImplTest {
    @Mock
    private ItemRepository itemRepositoryMock;
    @Mock
    private RestaurantRepository restaurantRepositoryMock;
    @Mock
    private AccessToken requestAccessToken;
    @InjectMocks
    private GetItemUseCaseImpl getItemUseCase;

    @Test
    void getItem_shouldReturnItemConverted() {
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

        ItemEntity itemEntity = ItemEntity.builder()
                .id(1L)
                .name("Burger")
                .price(12.3)
                .restaurant(restaurantEntity)
                .build();

        when(itemRepositoryMock.findById(itemEntity.getId())).thenReturn(Optional.of(itemEntity));
        when(requestAccessToken.getUserId()).thenReturn(userEntity.getId());
        when(restaurantRepositoryMock.findById(itemEntity.getRestaurant().getId())).thenReturn(Optional.ofNullable(restaurantEntity));

        Optional<Item> actualResult = getItemUseCase.getItem(itemEntity.getId());

        Item item = Item.builder()
                .id(1L)
                .name("Burger")
                .price(12.3)
                .restaurant_id(restaurantEntity.getId())
                .build();

        Optional<Item> expectedResult = Optional.of(item);
        assertEquals(expectedResult, actualResult);

        verify(itemRepositoryMock).findById(itemEntity.getId());
    }
}