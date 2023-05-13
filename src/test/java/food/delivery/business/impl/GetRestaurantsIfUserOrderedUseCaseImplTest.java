package food.delivery.business.impl;

import food.delivery.domain.AccessToken;
import food.delivery.domain.GetAllRestaurantsIfUserOrderedRequest;
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
class GetRestaurantsIfUserOrderedUseCaseImplTest {
    @Mock
    private RestaurantRepository restaurantRepositoryMock;
    @Mock
    private AccessToken requestAccessToken;
    @InjectMocks
    private GetRestaurantsIfUserOrderedUseCaseImpl getRestaurantsIfUserOrderedUseCase;

    @Test
    void getRestaurantsIfUserOrdered_shouldReturnRestaurantsConverted() {
        UserEntity firstUserEntity = UserEntity.builder()
                .id(1L)
                .first_name("Alex")
                .last_name("Terrible")
                .email("bra@gmail.com")
                .address("Cold Russia")
                .phone("+40736756631")
                .password("Bigterrible3")
                .build();
        firstUserEntity.setUserRoles(Set.of(UserRoleEntity.builder().user(firstUserEntity).role(RoleEnum.RESTAURANT).build()));

        UserEntity secondUserEntity = UserEntity.builder()
                .id(2L)
                .first_name("Alex")
                .last_name("Terrible")
                .email("bramin@gmail.com")
                .address("Cold Russia")
                .phone("+40736756631")
                .password("Bigterrible3")
                .build();
        secondUserEntity.setUserRoles(Set.of(UserRoleEntity.builder().user(secondUserEntity).role(RoleEnum.RESTAURANT).build()));

        UserEntity thirdUserEntity = UserEntity.builder()
                .id(3L)
                .first_name("Alex")
                .last_name("Terrible")
                .email("bruhh@gmail.com")
                .address("Cold Russia")
                .phone("+40736756631")
                .password("Bigterrible3")
                .build();
        thirdUserEntity.setUserRoles(Set.of(UserRoleEntity.builder().user(thirdUserEntity).role(RoleEnum.USER).build()));

        RestaurantEntity firstRestaurantEntity = RestaurantEntity.builder()
                .id(1L)
                .name("Munch")
                .address("Center")
                .user(firstUserEntity)
                .type("american")
                .build();

        RestaurantEntity secondRestaurantEntity = RestaurantEntity.builder()
                .id(2L)
                .name("Munch")
                .address("Center")
                .user(secondUserEntity)
                .type("american")
                .build();

        when(restaurantRepositoryMock.getRestaurantIfUserOrdered(thirdUserEntity.getId())).thenReturn(List.of(firstRestaurantEntity, secondRestaurantEntity));
        when(requestAccessToken.getUserId()).thenReturn(thirdUserEntity.getId());

        GetRestaurantsResponse actualResult = getRestaurantsIfUserOrderedUseCase.getRestaurantsIfUserOrdered(GetAllRestaurantsIfUserOrderedRequest.builder()
                .user_id(thirdUserEntity.getId()).build());

        Restaurant firstRestaurant = Restaurant.builder()
                .id(1L)
                .name("Munch")
                .address("Center")
                .user_id(firstUserEntity.getId())
                .type("american")
                .build();

        Restaurant secondRestaurant = Restaurant.builder()
                .id(2L)
                .name("Munch")
                .address("Center")
                .user_id(secondUserEntity.getId())
                .type("american")
                .build();

        GetRestaurantsResponse expectedResult = GetRestaurantsResponse.builder()
                .restaurants(List.of(firstRestaurant, secondRestaurant)).build();
        assertEquals(expectedResult, actualResult);

        verify(restaurantRepositoryMock).getRestaurantIfUserOrdered(thirdUserEntity.getId());
    }
}