package food.delivery.business;

import food.delivery.domain.GetAllOrdersByUserIdRequest;
import food.delivery.domain.GetOrdersResponse;

public interface GetOrdersByUserIdUseCase {
    GetOrdersResponse getOrdersByUserId(GetAllOrdersByUserIdRequest request);
}
