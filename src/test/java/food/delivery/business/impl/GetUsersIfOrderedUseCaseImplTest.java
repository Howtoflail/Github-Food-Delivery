package food.delivery.business.impl;

import food.delivery.domain.AccessToken;
import food.delivery.domain.GetUsersResponse;
import food.delivery.domain.User;
import food.delivery.persistence.RestaurantRepository;
import food.delivery.persistence.UserRepository;
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
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class GetUsersIfOrderedUseCaseImplTest {
    @Mock
    private UserRepository userRepositoryMock;
    @Mock
    private RestaurantRepository restaurantRepositoryMock;
    @Mock
    private AccessToken requestAccessToken;
    @InjectMocks
    private GetUsersIfOrderedUseCaseImpl getUsersIfOrderedUseCase;

    @Test
    void getUsersIfOrdered_shouldReturnUsersConverted() {
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

        UserEntity thirdUserEntity = UserEntity.builder()
                .id(3L)
                .first_name("Alex")
                .last_name("Terrible")
                .email("bra@gmail.com")
                .address("Cold Russia")
                .phone("+40736756631")
                .password("Bigterrible3")
                .build();
        thirdUserEntity.setUserRoles(Set.of(UserRoleEntity.builder().user(thirdUserEntity).role(RoleEnum.RESTAURANT).build()));

        RestaurantEntity restaurant = RestaurantEntity.builder().id(1L).name("Munch").address("Center").user(thirdUserEntity).type("american").build();

        when(userRepositoryMock.getUsersFromRestaurantIfOrdered(1L)).thenReturn(List.of(firstUserEntity, secondUserEntity));
        when(restaurantRepositoryMock.findById(1L)).thenReturn(Optional.ofNullable(restaurant));
        when(requestAccessToken.getUserId()).thenReturn(thirdUserEntity.getId());

        GetUsersResponse actualResult = getUsersIfOrderedUseCase.getUsersIfOrdered(1L);

        User firstUser = User.builder()
                .id(1L)
                .first_name("Tom")
                .last_name("Rolland")
                .email("tomrolland@gmail.com")
                .address("Buna Vestire 54")
                .phone("+40736756631")
                .password("Marele123")
                .build();

        User secondUser = User.builder()
                .id(2L)
                .first_name("Alex")
                .last_name("Terrible")
                .email("alexterrible@gmail.com")
                .address("Cold Russia")
                .phone("+40736756631")
                .password("Bigterrible3")
                .build();

        GetUsersResponse expectedResult = GetUsersResponse.builder()
                .users(List.of(firstUser, secondUser))
                .build();
        assertEquals(expectedResult, actualResult);

        verify(userRepositoryMock).getUsersFromRestaurantIfOrdered(1L);
    }
}