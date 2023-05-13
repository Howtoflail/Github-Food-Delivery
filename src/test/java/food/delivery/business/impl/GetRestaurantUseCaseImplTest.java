package food.delivery.business.impl;

import food.delivery.domain.Restaurant;
import food.delivery.persistence.RestaurantRepository;
import food.delivery.persistence.entity.RestaurantEntity;
import food.delivery.persistence.entity.RoleEnum;
import food.delivery.persistence.entity.UserEntity;
import food.delivery.persistence.entity.UserRoleEntity;
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
class GetRestaurantUseCaseImplTest {
    @Mock
    private RestaurantRepository restaurantRepositoryMock;
    @InjectMocks
    private GetRestaurantUseCaseImpl getRestaurantUseCase;

    @Test
    void getRestaurant_shouldReturnRestaurantConverted() {
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

        when(restaurantRepositoryMock.findById(restaurantEntity.getId())).thenReturn(Optional.of(restaurantEntity));

        Optional<Restaurant> actualResult = getRestaurantUseCase.getRestaurant(restaurantEntity.getId());

        Restaurant restaurant = Restaurant.builder()
                .id(1L)
                .name("Munch")
                .user_id(userEntity.getId())
                .address("Center")
                .type("japanese")
                .build();

        Optional<Restaurant> expectedResult = Optional.of(restaurant);
        assertEquals(expectedResult, actualResult);

        verify(restaurantRepositoryMock).findById(restaurantEntity.getId());
    }
}