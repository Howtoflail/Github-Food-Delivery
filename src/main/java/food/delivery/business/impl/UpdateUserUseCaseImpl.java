package food.delivery.business.impl;

import food.delivery.business.UpdateUserUseCase;
import food.delivery.business.exception.UnauthorizedDataAccessException;
import food.delivery.domain.AccessToken;
import food.delivery.domain.UpdateUserRequest;
import food.delivery.persistence.UserRepository;
import food.delivery.persistence.entity.RoleEnum;
import food.delivery.persistence.entity.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UpdateUserUseCaseImpl implements UpdateUserUseCase {
    private final UserRepository userRepository;
    private AccessToken requestAccessToken;
    @Transactional
    @Override
    public void updateUser(UpdateUserRequest request) {
        //get user id from front end
        Optional<UserEntity> userOptional = userRepository.findById(request.getId());

        //the user should always exist because all the users are listed and clicked on
        if(userOptional.isPresent()){
            UserEntity user = userOptional.get();

            if(!requestAccessToken.hasRole(RoleEnum.ADMIN.name())){
                if(!Objects.equals(requestAccessToken.getUserId(), user.getId())){
                    throw new UnauthorizedDataAccessException("USER_ID_NOT_FROM_LOGGED_IN_USER");
                }
            }

            updateFields(request, user);
        }
    }

    private void updateFields(UpdateUserRequest request, UserEntity user){
        user.setFirst_name(request.getFirst_name());
        user.setLast_name(request.getLast_name());
        user.setEmail(request.getEmail());
        user.setAddress(request.getAddress());
        user.setPhone(request.getPhone());
        user.setPassword(request.getPassword());

        userRepository.save(user);
    }
}
