package food.delivery.business.impl;

import food.delivery.domain.OrderItem;
import food.delivery.persistence.entity.ItemEntity;
import food.delivery.persistence.entity.OrderEntity;
import food.delivery.persistence.entity.OrderItemEntity;

final class OrderItemConverter {

    private OrderItemConverter(){}

    public static OrderItem convert(OrderItemEntity orderItem){
        OrderEntity order = orderItem.getOrder();
        ItemEntity item = orderItem.getItem();
        return OrderItem.builder().id(orderItem.getId()).order_id(order.getId()).item_id(item.getId()).build();
    }
}
