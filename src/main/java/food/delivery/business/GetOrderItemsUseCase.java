package food.delivery.business;

import food.delivery.domain.GetAllOrderItemsRequest;
import food.delivery.domain.GetOrderItemsResponse;

public interface GetOrderItemsUseCase {
    GetOrderItemsResponse getOrderItems(GetAllOrderItemsRequest request);
}
