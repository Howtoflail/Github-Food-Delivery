package food.delivery.business.impl;

import food.delivery.business.AccessTokenEncoder;
import food.delivery.business.LoginUseCase;
import food.delivery.business.exception.InvalidCredentialsException;
import food.delivery.domain.AccessToken;
import food.delivery.domain.LoginRequest;
import food.delivery.domain.LoginResponse;
import food.delivery.persistence.UserRepository;
import food.delivery.persistence.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LoginUseCaseImpl implements LoginUseCase {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccessTokenEncoder accessTokenEncoder;

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        UserEntity user = userRepository.queryByEmail(loginRequest.getEmail());
        if(user == null){
            throw new InvalidCredentialsException();
        }

        if(!matchesPassword(loginRequest.getPassword(), user.getPassword())){
            throw new InvalidCredentialsException();
        }

        String accessToken = generateAccessToken(user);
        return LoginResponse.builder().accessToken(accessToken).build();
    }

    private boolean matchesPassword(String rawPassword, String encodedPassword){
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    private String generateAccessToken(UserEntity user){
        //implement roles as in example
        List<String> roles = user.getUserRoles().stream()
                .map(userRole -> userRole.getRole().toString()).toList();

        return accessTokenEncoder.encode(AccessToken.builder()
                        .subject(user.getEmail())
                        .roles(roles)
                        .userId(user.getId())
                .build());
    }
}
