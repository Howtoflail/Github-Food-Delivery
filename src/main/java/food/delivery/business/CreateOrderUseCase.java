package food.delivery.business;

import food.delivery.domain.CreateOrderRequest;
import food.delivery.domain.CreateOrderResponse;

public interface CreateOrderUseCase {
    CreateOrderResponse createOrder(CreateOrderRequest request);
}
