package food.delivery.business.impl;

import food.delivery.domain.AccessToken;
import food.delivery.domain.User;
import food.delivery.persistence.UserRepository;
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
class GetUserUseCaseImplTest {
    @Mock
    private UserRepository userRepositoryMock;
    @Mock
    private AccessToken requestAccessToken;
    @InjectMocks
    private GetUserUseCaseImpl getUserUseCase;

    @Test
    void getUser_shouldReturnUserConverted() {
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

        when(userRepositoryMock.findById(userEntity.getId())).thenReturn(Optional.of(userEntity));
        when(requestAccessToken.getUserId()).thenReturn(userEntity.getId());

        Optional<User> actualResult = getUserUseCase.getUser(userEntity.getId());

        User user = User.builder()
                .id(1L)
                .first_name("Tom")
                .last_name("Rolland")
                .email("tomrolland@gmail.com")
                .address("Buna Vestire 54")
                .phone("+40736756631")
                .password("Marele123")
                .build();

        Optional<User> expectedResult = Optional.of(user);
        assertEquals(expectedResult, actualResult);

        verify(userRepositoryMock).findById(user.getId());
    }
}