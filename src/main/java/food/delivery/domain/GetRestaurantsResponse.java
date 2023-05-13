package food.delivery.domain;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GetRestaurantsResponse {
    private List<Restaurant> restaurants;
}
