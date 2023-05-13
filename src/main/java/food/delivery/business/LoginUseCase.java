package food.delivery.business;

import food.delivery.domain.LoginRequest;
import food.delivery.domain.LoginResponse;

public interface LoginUseCase {
    LoginResponse login(LoginRequest loginRequest);
}
