package food.delivery.business.impl;

import food.delivery.configuration.security.PasswordEncoderConfig;
import food.delivery.domain.CreateUserRequest;
import food.delivery.domain.CreateUserResponse;
import food.delivery.persistence.UserRepository;
import food.delivery.persistence.entity.RoleEnum;
import food.delivery.persistence.entity.UserEntity;
import food.delivery.persistence.entity.UserRoleEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
//@RunWith(SpringJUnit4ClassRunner.class)
@ExtendWith(MockitoExtension.class)
@SpringBootTest
//@EnableAutoConfiguration(exclude = PasswordEncoderConfig.class)
//@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
class CreateUserUseCaseImplTest {
    //Configurations in order to make password encoder work
//    @Autowired
//    private static UserRepository userRepositoryConfiguration;
//
//    @Autowired
//    private static PasswordEncoderConfig passwordEncoderConfiguration;
//    @Configuration
//    static class ContextConfiguration {
//        @Bean
//        public CreateUserUseCaseImpl createUserUseCaseImpl() {
//            CreateUserUseCaseImpl createUserUseCaseImpl = new CreateUserUseCaseImpl(userRepositoryConfiguration, passwordEncoderConfiguration);
//            return createUserUseCaseImpl;
//        }
//    }
    @Mock
    private UserRepository userRepositoryMock;
    //@Autowired

    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private CreateUserUseCaseImpl createUserUseCase;

    @Test
    void createUser_shouldSaveUserWithAllFields() {
        //String encodedPassword = passwordEncoder.createBCryptPasswordEncoder().encode("Marele123");

        UserEntity userEntity = UserEntity.builder()
                //.id(1L)
                .first_name("Tom")
                .last_name("Rolland")
                .email("tomrolland@gmail.com")
                .address("Buna Vestire 54")
                .phone("+40736756631")
                .password("rand")
                .build();
        userEntity.setUserRoles(Set.of(UserRoleEntity.builder().user(userEntity).role(RoleEnum.USER).build()));

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
        CreateUserResponse actualResult = createUserUseCase.createUser(request);

        CreateUserResponse expectedResult = CreateUserResponse.builder()
                .userId(userEntity.getId())
                .build();
        assertEquals(expectedResult, actualResult);

        verify(userRepositoryMock).save(userEntity);
        verify(userRepositoryMock).save(Mockito.any());
    }
}