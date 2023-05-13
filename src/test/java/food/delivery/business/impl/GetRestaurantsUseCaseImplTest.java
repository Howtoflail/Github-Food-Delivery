package food.delivery.business.impl;

import food.delivery.domain.GetRestaurantsResponse;
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

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class GetRestaurantsUseCaseImplTest {
    @Mock
    private RestaurantRepository restaurantRepositoryMock;
    @InjectMocks
    private GetRestaurantsUseCaseImpl getRestaurantsUseCase;

    @Test
    void getRestaurants_shouldReturnAllRestaurantsConverted() {
        UserEntity firstUserEntity = UserEntity.builder()
                .id(1L)
                .first_name("Tom")
                .last_name("Rolland")
                .email("tomrolland@gmail.com")
                .address("Buna Vestire 54")
                .phone("+40736756631")
                .password("Marele123")
                .build();
        firstUserEntity.setUserRoles(Set.of(UserRoleEntity.builder().user(firstUserEntity).role(RoleEnum.RESTAURANT).build()));

        UserEntity secondUserEntity = UserEntity.builder()
                .id(2L)
                .first_name("Ron")
                .last_name("Charles")
                .email("tomrolland@gmail.com")
                .address("Buna Vestire 54")
                .phone("+40736756631")
                .password("Marele123")
                .build();
        secondUserEntity.setUserRoles(Set.of(UserRoleEntity.builder().user(secondUserEntity).role(RoleEnum.RESTAURANT).build()));

        RestaurantEntity firstRestaurantEntity = RestaurantEntity.builder()
                .id(1L)
                .name("Munch")
                .user(firstUserEntity)
                .address("Center")
                .type("japanese")
                .build();

        RestaurantEntity secondRestaurantEntity = RestaurantEntity.builder()
                .id(2L)
                .name("Jalapenos")
                .user(secondUserEntity)
                .address("Center")
                .type("american")
                .build();

        when(restaurantRepositoryMock.findAll()).thenReturn(List.of(firstRestaurantEntity, secondRestaurantEntity));

        GetRestaurantsResponse actualResult = getRestaurantsUseCase.getRestaurants();

        Restaurant firstRestaurant = Restaurant.builder()
                .id(1L)
                .name("Munch")
                .user_id(firstUserEntity.getId())
                .address("Center")
                .type("japanese")
                .build();

        Restaurant secondRestaurant = Restaurant.builder()
                .id(2L)
                .name("Jalapenos")
                .user_id(secondUserEntity.getId())
                .address("Center")
                .type("american")
                .build();

        GetRestaurantsResponse expectedResult = GetRestaurantsResponse.builder()
                .restaurants(List.of(firstRestaurant, secondRestaurant))
                .build();
        assertEquals(expectedResult, actualResult);

        verify(restaurantRepositoryMock).findAll();
    }
}