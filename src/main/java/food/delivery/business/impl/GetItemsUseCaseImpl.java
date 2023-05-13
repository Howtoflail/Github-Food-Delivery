package food.delivery.business.impl;

import food.delivery.business.GetItemsUseCase;
import food.delivery.business.exception.UnauthorizedDataAccessException;
import food.delivery.domain.AccessToken;
import food.delivery.domain.GetAllItemsRequest;
import food.delivery.domain.GetItemsResponse;
import food.delivery.domain.Item;
import food.delivery.persistence.ItemRepository;
import food.delivery.persistence.RestaurantRepository;
import food.delivery.persistence.entity.RestaurantEntity;
import food.delivery.persistence.entity.RoleEnum;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class GetItemsUseCaseImpl implements GetItemsUseCase {
    private ItemRepository itemRepository;
//    private RestaurantRepository restaurantRepository;
//    private AccessToken requestAccessToken;

    @Transactional
    @Override
    public GetItemsResponse getItems(final GetAllItemsRequest request) {
//        Optional<RestaurantEntity> optionalRestaurant = restaurantRepository.findById(request.getRestaurant_id());
//        if(optionalRestaurant.isPresent()){
//            RestaurantEntity restaurant = optionalRestaurant.get();
//
//            if(!requestAccessToken.hasRole(RoleEnum.ADMIN.name())){
//                if(requestAccessToken.getUserId() != restaurant.getUser().getId()){
//                    throw new UnauthorizedDataAccessException("USER_ID_NOT_FROM_LOGGED_IN_USER");
//                }
//            }
//        }

        List<Item> items;
        if(request.getRestaurant_id() != null){
            items = itemRepository.findAllByRestaurant_IdOrderByIdAsc(request.getRestaurant_id()).stream()
                    .map(ItemConverter::convert).toList();
        }
        else {
            items = itemRepository.findAll().stream().map(ItemConverter::convert).toList();
        }

        return GetItemsResponse.builder().items(items).build();
    }
}
