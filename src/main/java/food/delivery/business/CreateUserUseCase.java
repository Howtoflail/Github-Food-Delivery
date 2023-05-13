package food.delivery.business;

import food.delivery.domain.CreateUserRequest;
import food.delivery.domain.CreateUserResponse;

public interface CreateUserUseCase {

    CreateUserResponse createUser(CreateUserRequest request);
}
