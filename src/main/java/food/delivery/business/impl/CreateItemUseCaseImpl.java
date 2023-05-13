package food.delivery.business.impl;

import food.delivery.business.CreateItemUseCase;
import food.delivery.business.exception.ItemNameAlreadyExistsException;
import food.delivery.business.exception.UnauthorizedDataAccessException;
import food.delivery.domain.AccessToken;
import food.delivery.domain.CreateItemRequest;
import food.delivery.domain.CreateItemResponse;
import food.delivery.persistence.ItemRepository;
import food.delivery.persistence.RestaurantRepository;
import food.delivery.persistence.entity.ItemEntity;
import food.delivery.persistence.entity.RestaurantEntity;
import food.delivery.persistence.entity.RoleEnum;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CreateItemUseCaseImpl implements CreateItemUseCase {
    private ItemRepository itemRepository;
    private RestaurantRepository restaurantRepository;
    private AccessToken requestAccessToken;

    @Transactional
    @Override
    public CreateItemResponse createItem(CreateItemRequest request) {
        if(itemRepository.findByName(request.getName()) != null){
            throw new ItemNameAlreadyExistsException();
        }

        ItemEntity savedItem = saveNewItem(request);

        assert savedItem != null;
        return CreateItemResponse.builder().itemId(savedItem.getId()).build();
    }

    private ItemEntity saveNewItem(CreateItemRequest request){
        //throw exception here if restaurant doesn't exist
        Optional<RestaurantEntity> restaurantOptional = restaurantRepository.findById(request.getRestaurant_id());
        if(restaurantOptional.isPresent()){
            RestaurantEntity restaurant = restaurantOptional.get();

            if(!requestAccessToken.hasRole(RoleEnum.ADMIN.name())){
                if(!Objects.equals(requestAccessToken.getUserId(), restaurant.getUser().getId())){
                    throw new UnauthorizedDataAccessException("USER_ID_NOT_FROM_LOGGED_IN_USER");
                }
            }

            ItemEntity newItem = ItemEntity.builder().name(request.getName()).price(request.getPrice()).restaurant(restaurant).build();
            return itemRepository.save(newItem);
        }
        return null;
    }
}
