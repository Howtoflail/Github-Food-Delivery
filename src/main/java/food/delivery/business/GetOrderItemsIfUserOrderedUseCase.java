package food.delivery.business;

import food.delivery.domain.GetAllOrderItemsIfUserOrderedRequest;
import food.delivery.domain.GetOrderItemsResponse;

public interface GetOrderItemsIfUserOrderedUseCase {
    GetOrderItemsResponse getOrderItemsIfUserOrdered(GetAllOrderItemsIfUserOrderedRequest request);
}
