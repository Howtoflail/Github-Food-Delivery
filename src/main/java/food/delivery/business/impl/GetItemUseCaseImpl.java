package food.delivery.business.impl;

import food.delivery.business.GetItemUseCase;
import food.delivery.business.exception.UnauthorizedDataAccessException;
import food.delivery.domain.AccessToken;
import food.delivery.domain.Item;
import food.delivery.domain.Restaurant;
import food.delivery.persistence.ItemRepository;
import food.delivery.persistence.RestaurantRepository;
import food.delivery.persistence.entity.RestaurantEntity;
import food.delivery.persistence.entity.RoleEnum;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class GetItemUseCaseImpl implements GetItemUseCase {
    private ItemRepository itemRepository;
    private RestaurantRepository restaurantRepository;
    private AccessToken requestAccessToken;

    @Transactional
    @Override
    public Optional<Item> getItem(Long itemId) {
        Optional<Item> optionalItem = itemRepository.findById(itemId).map(ItemConverter::convert);
        if(optionalItem.isPresent()){
            Item item = optionalItem.get();

            Optional<RestaurantEntity> optionalRestaurant = restaurantRepository.findById(item.getRestaurant_id());
            if(optionalRestaurant.isPresent()){
                RestaurantEntity restaurant = optionalRestaurant.get();

                if(!requestAccessToken.hasRole(RoleEnum.ADMIN.name())){
                    if(!Objects.equals(requestAccessToken.getUserId(), restaurant.getUser().getId())){
                        throw new UnauthorizedDataAccessException("USER_ID_NOT_FROM_LOGGED_IN_USER");
                    }
                }
                return optionalItem;
            }
        }
        return Optional.empty();
    }
}

