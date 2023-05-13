package food.delivery.business;

import food.delivery.domain.GetAllRestaurantsIfUserOrderedRequest;
import food.delivery.domain.GetRestaurantsResponse;

public interface GetRestaurantsIfUserOrderedUseCase {
    GetRestaurantsResponse getRestaurantsIfUserOrdered(GetAllRestaurantsIfUserOrderedRequest request);
}
