package food.delivery.business.impl;

import food.delivery.domain.Item;
import food.delivery.persistence.entity.ItemEntity;
import food.delivery.persistence.entity.RestaurantEntity;

final class ItemConverter {

    private ItemConverter(){}

    public static Item convert(ItemEntity item){
        RestaurantEntity restaurant = item.getRestaurant();
        return Item.builder().id(item.getId()).name(item.getName()).price(item.getPrice()).restaurant_id(restaurant.getId()).build();
    }
}
