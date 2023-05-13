package food.delivery.business;

import food.delivery.domain.OrderItem;

import java.util.Optional;

public interface GetOrderItemUseCase {
    Optional<OrderItem> getOrderItem(Long orderItemId);
}
