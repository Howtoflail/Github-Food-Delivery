package food.delivery.business;

import food.delivery.domain.GetUsersResponse;

public interface GetUsersIfOrderedUseCase {
    GetUsersResponse getUsersIfOrdered(Long restaurantId);
}
