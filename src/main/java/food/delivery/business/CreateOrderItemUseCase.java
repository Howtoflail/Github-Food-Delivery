package food.delivery.business;

import food.delivery.domain.CreateOrderItemRequest;
import food.delivery.domain.CreateOrderItemResponse;

public interface CreateOrderItemUseCase {
    CreateOrderItemResponse createOrderItem(CreateOrderItemRequest request);
}
