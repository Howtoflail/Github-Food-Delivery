package food.delivery.business;

import food.delivery.domain.GetAllItemsRequest;
import food.delivery.domain.GetItemsResponse;

public interface GetItemsUseCase {
    GetItemsResponse getItems(GetAllItemsRequest request);
}
