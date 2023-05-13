package food.delivery.business.impl;

import food.delivery.domain.AccessToken;
import food.delivery.persistence.ItemRepository;
import food.delivery.persistence.entity.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class DeleteItemUseCaseImplTest {
    @Mock
    private ItemRepository itemRepositoryMock;
    @Mock
    private AccessToken requestAccessToken;
    @InjectMocks
    private DeleteItemUseCaseImpl deleteItemUseCase;

    @Test
    void deleteItem_shouldDeleteItem() {
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

        ItemEntity itemEntity = ItemEntity.builder()
                .id(1L)
                .name("Burger")
                .price(12.3)
                .restaurant(restaurantEntity)
                .build();

        deleteItemUseCase.deleteItem(itemEntity.getId());

        verify(itemRepositoryMock).deleteById(itemEntity.getId());
    }
}