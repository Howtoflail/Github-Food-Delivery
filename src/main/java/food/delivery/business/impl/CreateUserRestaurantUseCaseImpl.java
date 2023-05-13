package food.delivery.business.impl;

import food.delivery.business.CreateUserRestaurantUseCase;
import food.delivery.business.exception.UserEmailAlreadyExistsException;
import food.delivery.domain.CreateUserRequest;
import food.delivery.domain.CreateUserResponse;
import food.delivery.persistence.UserRepository;
import food.delivery.persistence.entity.RoleEnum;
import food.delivery.persistence.entity.UserEntity;
import food.delivery.persistence.entity.UserRoleEntity;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Set;

@Service
@AllArgsConstructor
public class CreateUserRestaurantUseCaseImpl implements CreateUserRestaurantUseCase {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    @Transactional
    @Override
    public CreateUserResponse createUserRestaurant(CreateUserRequest request) {
        if(userRepository.findByEmail(request.getEmail()) != null) {
            throw new UserEmailAlreadyExistsException();
        }

        UserEntity savedUser = saveNewUser(request);

        return CreateUserResponse.builder()
                .userId(savedUser.getId())
                .build();
    }

    private UserEntity saveNewUser(CreateUserRequest request){
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        UserEntity newUser = UserEntity.builder()
                .first_name(request.getFirst_name())
                .last_name(request.getLast_name())
                .email(request.getEmail())
                .address(request.getAddress())
                .phone(request.getPhone())
                .password(encodedPassword)
                .build();

        newUser.setUserRoles(Set.of(UserRoleEntity.builder()
                .user(newUser).role(RoleEnum.RESTAURANT).build()));

        return userRepository.save(newUser);
    }
}
