package food.delivery.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateItemResponse {
    private Long itemId;
}
