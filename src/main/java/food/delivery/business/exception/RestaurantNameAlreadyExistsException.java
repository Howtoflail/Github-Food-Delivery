package food.delivery.business.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class RestaurantNameAlreadyExistsException extends ResponseStatusException {
    public RestaurantNameAlreadyExistsException() {
        super(HttpStatus.BAD_REQUEST, "RESTAURANT_WITH_GIVEN_NAME_ALREADY_EXISTS");
    }
}
