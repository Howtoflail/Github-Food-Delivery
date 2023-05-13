package food.delivery.business.impl;

import food.delivery.business.UpdateItemUseCase;
import food.delivery.business.exception.UnauthorizedDataAccessException;
import food.delivery.domain.AccessToken;
import food.delivery.domain.UpdateItemRequest;
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
public class UpdateItemUseCaseImpl implements UpdateItemUseCase {
    private ItemRepository itemRepository;
    private RestaurantRepository restaurantRepository;
    private AccessToken requestAccessToken;

    @Transactional
    @Override
    public void updateItem(UpdateItemRequest request) {
        //get itemId from frontend
        Optional<ItemEntity> itemOptional = itemRepository.findById(request.getId());

        if(itemOptional.isPresent()){
            ItemEntity item = itemOptional.get();
            updateFields(request, item);
        }
    }

    private void updateFields(UpdateItemRequest request, ItemEntity item){
        item.setId(request.getId());
        item.setName(request.getName());
        item.setPrice(request.getPrice());
        //make restaurant id fixed and not changeable by user in frontend
        Optional<RestaurantEntity> restaurantOptional = restaurantRepository.findById(request.getRestaurant_id());
        if(restaurantOptional.isPresent()){
            RestaurantEntity restaurant = restaurantOptional.get();

            if(!requestAccessToken.hasRole(RoleEnum.ADMIN.name())){
                if(!Objects.equals(requestAccessToken.getUserId(), restaurant.getUser().getId())){
                    throw new UnauthorizedDataAccessException("USER_ID_NOT_FROM_LOGGED_IN_USER");
                }
            }

            item.setRestaurant(restaurant);
            itemRepository.save(item);
        }
    }
}
