package food.delivery.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateRestaurantRequest {
    @NotBlank
    private String name;

    @NotBlank
    private String type;

    @NotBlank
    private String address;
}
