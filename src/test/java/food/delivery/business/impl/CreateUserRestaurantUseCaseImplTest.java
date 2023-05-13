package food.delivery.business.impl;

import food.delivery.domain.CreateUserRequest;
import food.delivery.domain.CreateUserResponse;
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

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class CreateUserRestaurantUseCaseImplTest {
    @Mock
    private UserRepository userRepositoryMock;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private CreateUserRestaurantUseCaseImpl createUserRestaurantUseCase;

    @Test
    void createUserRestaurant_shouldSaveUserWithAllFields() {
        UserEntity userEntity = UserEntity.builder()
                .first_name("Tom")
                .last_name("Rolland")
                .email("tomrolland@gmail.com")
                .address("Buna Vestire 54")
                .phone("+40736756631")
                .password("rand")
                .build();
        userEntity.setUserRoles(Set.of(UserRoleEntity.builder().user(userEntity).role(RoleEnum.RESTAURANT).build()));

        when(passwordEncoder.encode(Mockito.any())).thenReturn("rand");
        when(userRepositoryMock.save(userEntity)).thenReturn(userEntity);

        CreateUserRequest request = CreateUserRequest.builder()
                .first_name("Tom")
                .last_name("Rolland")
                .email("tomrolland@gmail.com")
                .address("Buna Vestire 54")
                .phone("+40736756631")
                .password("rand")
                .build();
        CreateUserResponse actualResult = createUserRestaurantUseCase.createUserRestaurant(request);

        CreateUserResponse expectedResult = CreateUserResponse.builder()
                .userId(userEntity.getId())
                .build();
        assertEquals(expectedResult, actualResult);

        verify(userRepositoryMock).save(userEntity);
        verify(userRepositoryMock).save(Mockito.any());
    }
}