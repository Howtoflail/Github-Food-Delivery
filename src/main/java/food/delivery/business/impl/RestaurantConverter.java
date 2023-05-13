package food.delivery.business.impl;

import food.delivery.domain.Restaurant;
import food.delivery.persistence.entity.RestaurantEntity;
import food.delivery.persistence.entity.UserEntity;

final class RestaurantConverter {
    private RestaurantConverter(){}

    public static Restaurant convert(RestaurantEntity restaurant){
        UserEntity user = restaurant.getUser();
        return Restaurant.builder().id(restaurant.getId())
                .name(restaurant.getName())
                .user_id(user.getId())
                .address(restaurant.getAddress())
                .type(restaurant.getType()).build();
    }
}
