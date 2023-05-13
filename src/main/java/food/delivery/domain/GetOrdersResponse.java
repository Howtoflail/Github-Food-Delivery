package food.delivery.domain;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GetOrdersResponse {
    private List<Order> orders;
}
