package food.delivery.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRestaurantRequest {
    private Long id;

    @NotBlank
    private String name;

    private String type;

    @NotBlank
    private String address;
    //user id should remain the same
}
