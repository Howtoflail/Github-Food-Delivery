package food.delivery.business;

import food.delivery.domain.CreateItemRequest;
import food.delivery.domain.CreateItemResponse;

public interface CreateItemUseCase {

    CreateItemResponse createItem(CreateItemRequest request);
}
