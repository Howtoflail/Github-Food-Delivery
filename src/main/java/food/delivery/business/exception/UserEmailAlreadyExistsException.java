package food.delivery.business.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UserEmailAlreadyExistsException extends ResponseStatusException {
    public UserEmailAlreadyExistsException() {
        super(HttpStatus.BAD_REQUEST, "EMAIL_ALREADY_EXISTS");
    }
}
