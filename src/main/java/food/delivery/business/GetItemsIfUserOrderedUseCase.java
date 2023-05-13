package food.delivery.business;

import food.delivery.domain.GetAllItemsIfUserOrderedRequest;
import food.delivery.domain.GetItemsResponse;

public interface GetItemsIfUserOrderedUseCase {
    GetItemsResponse getItemsIfUserOrdered(GetAllItemsIfUserOrderedRequest request);
}
