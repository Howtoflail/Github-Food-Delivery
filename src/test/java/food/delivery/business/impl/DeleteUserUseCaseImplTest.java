package food.delivery.business.impl;

import food.delivery.domain.AccessToken;
import food.delivery.persistence.UserRepository;
import food.delivery.persistence.UserRoleRepository;
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
//@SpringBootTest
class DeleteUserUseCaseImplTest {
    @Mock
    private UserRepository userRepositoryMock;
    @Mock
    private UserRoleRepository userRoleRepositoryMock;
    @Mock
    private AccessToken requestAccessToken;
    @InjectMocks
    private DeleteUserUseCaseImpl deleteUserUseCase;

    @Test
    void deleteUser_shouldDeleteUser() {
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

        //when(userRepositoryMock.findById(userEntity.getId())).thenReturn(Optional.of(userEntity)).thenReturn(null);
        when(requestAccessToken.getUserId()).thenReturn(userEntity.getId());

        deleteUserUseCase.deleteUser(userEntity.getId());

        verify(userRoleRepositoryMock).deleteByUser_Id(userEntity.getId());
//        verify(userRepositoryMock).deleteById(userEntity.getId());
    }
}