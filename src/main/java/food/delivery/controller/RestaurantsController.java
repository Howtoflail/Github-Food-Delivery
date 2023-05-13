package food.delivery.controller;

import food.delivery.business.*;
import food.delivery.configuration.security.isauthenticated.IsAuthenticated;
import food.delivery.domain.*;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/restaurants")
@AllArgsConstructor
public class RestaurantsController {
    private final CreateRestaurantUseCase createRestaurantUseCase;
    private final GetRestaurantUseCase getRestaurantUseCase;
    private final GetRestaurantsIfUserOrderedUseCase getRestaurantsIfUserOrderedUseCase;
    private final GetRestaurantsUseCase getRestaurantsUseCase;
    private final DeleteRestaurantUseCase deleteRestaurantUseCase;
    private final UpdateRestaurantUseCase updateRestaurantUseCase;

    @IsAuthenticated
    @RolesAllowed({"ROLE_USER", "ROLE_RESTAURANT", "ROLE_ADMIN"})
    @GetMapping("{id}")
    public ResponseEntity<Restaurant> getRestaurant(@PathVariable(value = "id") final Long id){
        final Optional<Restaurant> restaurantOptional = getRestaurantUseCase.getRestaurant(id);
        if(restaurantOptional.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(restaurantOptional.get());
    }

    //ADD TO TEST
    @IsAuthenticated
    @RolesAllowed({"ROLE_USER", "ROLE_ADMIN"})
    @GetMapping(value = "getRestaurantsFromUser")
    public ResponseEntity<GetRestaurantsResponse> getRestaurantIfUserOrdered(@RequestParam(value = "user", required = true) Long user_id){
        GetAllRestaurantsIfUserOrderedRequest request = new GetAllRestaurantsIfUserOrderedRequest();
        request.setUser_id(user_id);
        return ResponseEntity.ok(getRestaurantsIfUserOrderedUseCase.getRestaurantsIfUserOrdered(request));
    }

    @IsAuthenticated
    @RolesAllowed({"ROLE_USER", "ROLE_RESTAURANT", "ROLE_ADMIN"})
    @GetMapping()
    public ResponseEntity<GetRestaurantsResponse> getRestaurants(){ return ResponseEntity.ok(getRestaurantsUseCase.getRestaurants());}

    @PostMapping(value = "/save")
    public ResponseEntity<CreateRestaurantResponse> createRestaurant(@RequestBody @Valid CreateRestaurantRequest request){
        CreateRestaurantResponse response = createRestaurantUseCase.createRestaurant(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @IsAuthenticated
    @RolesAllowed({"ROLE_RESTAURANT", "ROLE_ADMIN"})
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteRestaurant(@PathVariable Long id){
        deleteRestaurantUseCase.deleteRestaurant(id);
        return ResponseEntity.noContent().build();
    }

    @IsAuthenticated
    @RolesAllowed({"ROLE_RESTAURANT", "ROLE_ADMIN"})
    @PutMapping(value = "/update/{id}")
    public ResponseEntity<Void> updateRestaurant(@PathVariable Long id, @RequestBody @Valid UpdateRestaurantRequest request){
        request.setId(id);
        updateRestaurantUseCase.updateRestaurant(request);
        return ResponseEntity.noContent().build();
    }
}
