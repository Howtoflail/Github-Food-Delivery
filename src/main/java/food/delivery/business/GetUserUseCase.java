package food.delivery.business;

import food.delivery.domain.User;

import java.util.Optional;

public interface GetUserUseCase {
    Optional<User> getUser(Long userId);
}
