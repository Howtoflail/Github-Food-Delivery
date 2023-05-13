package food.delivery.business.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ItemNameAlreadyExistsException extends ResponseStatusException {
    public ItemNameAlreadyExistsException() {
        super(HttpStatus.BAD_REQUEST, "ITEM_WITH_GIVEN_NAME_ALREADY_EXISTS");
    }
}
