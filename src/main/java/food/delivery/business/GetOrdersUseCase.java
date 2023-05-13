package food.delivery.business;

import food.delivery.domain.GetAllOrdersRequest;
import food.delivery.domain.GetOrdersResponse;

public interface GetOrdersUseCase {
    GetOrdersResponse getOrders(GetAllOrdersRequest request);
}
