package food.delivery.business.impl;

import food.delivery.business.DeleteItemUseCase;
import food.delivery.business.exception.UnauthorizedDataAccessException;
import food.delivery.domain.AccessToken;
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
public class DeleteItemUseCaseImpl implements DeleteItemUseCase {
    private ItemRepository itemRepository;
    private AccessToken requestAccessToken;

    @Transactional
    @Override
    public void deleteItem(Long itemId) {
        Optional<ItemEntity> optionalItem = itemRepository.findById(itemId);
        if(optionalItem.isPresent()){
            ItemEntity item = optionalItem.get();

            if(!requestAccessToken.hasRole(RoleEnum.ADMIN.name())){
                if(!Objects.equals(requestAccessToken.getUserId(), item.getRestaurant().getUser().getId())){
                    throw new UnauthorizedDataAccessException("USER_ID_NOT_FROM_LOGGED_IN_USER");
                }
            }
        }

        this.itemRepository.deleteById(itemId);
    }
}
