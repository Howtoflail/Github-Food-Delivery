package food.delivery.business.impl;

import food.delivery.business.GetUsersUseCase;
import food.delivery.domain.GetUsersResponse;
import food.delivery.domain.User;
import food.delivery.persistence.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GetUsersUseCaseImpl implements GetUsersUseCase {
    private final UserRepository userRepository;

    @Override
    public GetUsersResponse getUsers() {
        List<User> users = userRepository.findAll()
                .stream()
                .map(UserConverter::convert)
                .toList();

        return GetUsersResponse.builder()
                .users(users)
                .build();
    }
}
