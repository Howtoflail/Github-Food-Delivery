package food.delivery.business;

import food.delivery.domain.UpdateItemRequest;

public interface UpdateItemUseCase {
    void updateItem(UpdateItemRequest request);
}
