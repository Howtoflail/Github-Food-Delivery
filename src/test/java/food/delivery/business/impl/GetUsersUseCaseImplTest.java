package food.delivery.business.impl;

import food.delivery.domain.GetUsersResponse;
import food.delivery.domain.User;
import food.delivery.persistence.UserRepository;
import food.delivery.persistence.entity.RoleEnum;
import food.delivery.persistence.entity.UserEntity;
import food.delivery.persistence.entity.UserRoleEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
//@SpringBootTest
class GetUsersUseCaseImplTest {

    @Mock
    private UserRepository userRepositoryMock;
//    @Mock
//    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private GetUsersUseCaseImpl getUsersUseCase;
//    @InjectMocks
//    private CreateUserUseCaseImpl createUserUseCase;

    @Test
    void getUsers_shouldReturnAllUsersConverted() {
        //UserRepository userRepositoryMock = mock(UserRepository.class);

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

        when(userRepositoryMock.findAll()).thenReturn(List.of(firstUserEntity, secondUserEntity));

        GetUsersResponse actualResult = getUsersUseCase.getUsers();

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

        verify(userRepositoryMock).findAll();
    }
}