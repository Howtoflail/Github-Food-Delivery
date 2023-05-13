package food.delivery.business;

import food.delivery.domain.Order;

import java.util.Optional;

public interface GetOrderUseCase {
    Optional<Order> getOrder(Long orderId);
}
