package food.delivery.business.impl;

import food.delivery.business.GetItemsIfUserOrderedUseCase;
import food.delivery.business.exception.UnauthorizedDataAccessException;
import food.delivery.domain.AccessToken;
import food.delivery.domain.GetAllItemsIfUserOrderedRequest;
import food.delivery.domain.GetItemsResponse;
import food.delivery.domain.Item;
import food.delivery.persistence.ItemRepository;
import food.delivery.persistence.entity.RoleEnum;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class GetItemsIfUserOrderedUseCaseImpl implements GetItemsIfUserOrderedUseCase {
    private ItemRepository itemRepository;
    private AccessToken requestAccessToken;

    @Override
    public GetItemsResponse getItemsIfUserOrdered(GetAllItemsIfUserOrderedRequest request) {
        if(!requestAccessToken.hasRole(RoleEnum.ADMIN.name())){
            if(!Objects.equals(requestAccessToken.getUserId(), request.getUser_id())){
                throw new UnauthorizedDataAccessException("USER_ID_NOT_FROM_LOGGED_IN_USER");
            }
        }

        List<Item> items;
        if(request.getUser_id() != null){
            items = itemRepository.getAllItemsIfUserOrdered(request.getUser_id()).stream().map(ItemConverter::convert).toList();
        }
        else{
            items = itemRepository.findAll().stream().map(ItemConverter::convert).toList();
        }

        return GetItemsResponse.builder().items(items).build();
    }
}
