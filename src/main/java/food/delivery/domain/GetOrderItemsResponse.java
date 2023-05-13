package food.delivery.domain;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GetOrderItemsResponse {
    private List<OrderItem> orderItems;
}
