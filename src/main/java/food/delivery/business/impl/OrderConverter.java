package food.delivery.business.impl;

import food.delivery.domain.Order;
import food.delivery.persistence.entity.OrderEntity;
import food.delivery.persistence.entity.RestaurantEntity;
import food.delivery.persistence.entity.UserEntity;

final class OrderConverter {
    private OrderConverter(){}

    public static Order convert(OrderEntity order){
        UserEntity user = order.getUser();
        RestaurantEntity restaurant = order.getRestaurant();
        return Order.builder().id(order.getId()).user_id(user.getId()).restaurant_id(restaurant.getId()).dateTime(order.getDateTime()).build();
    }
}
