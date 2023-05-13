package food.delivery.business;

import food.delivery.domain.Item;

import java.util.Optional;

public interface GetItemUseCase {
    Optional<Item> getItem(Long itemId);
}
