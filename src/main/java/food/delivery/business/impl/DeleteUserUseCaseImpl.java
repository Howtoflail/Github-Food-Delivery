package food.delivery.business.impl;

import food.delivery.business.DeleteUserUseCase;
import food.delivery.business.exception.UnauthorizedDataAccessException;
import food.delivery.domain.AccessToken;
import food.delivery.persistence.UserRepository;
import food.delivery.persistence.UserRoleRepository;
import food.delivery.persistence.entity.RoleEnum;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Objects;

@Service
@AllArgsConstructor
public class DeleteUserUseCaseImpl implements DeleteUserUseCase {
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private AccessToken requestAccessToken;

    @Transactional
    @Override
    public void deleteUser(Long userId) {
        if(!requestAccessToken.hasRole(RoleEnum.ADMIN.name())){
            if(!Objects.equals(requestAccessToken.getUserId(), userId)){
                throw new UnauthorizedDataAccessException("USER_ID_NOT_FROM_LOGGED_IN_USER");
            }
        }

        this.userRoleRepository.deleteByUser_Id(userId);
        //this.userRepository.deleteById(userId);
    }
}
