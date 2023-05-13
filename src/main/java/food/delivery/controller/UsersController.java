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
@RequestMapping("/users")
@AllArgsConstructor
public class UsersController {
    private final GetUserUseCase getUserUseCase;
    private final GetUsersUseCase getUsersUseCase;
    private final GetRestaurantIdUseCase getRestaurantIdUseCase;
    private final GetUsersIfOrderedUseCase getUsersIfOrderedUseCase;
    private final CreateUserUseCase createUserUseCase;
    private final CreateUserRestaurantUseCase createUserRestaurantUseCase;
    private final DeleteUserUseCase deleteUserUseCase;
    private final UpdateUserUseCase updateUserUseCase;

    @GetMapping("{id}")
    @IsAuthenticated
    @RolesAllowed({"ROLE_USER", "ROLE_RESTAURANT", "ROLE_ADMIN"})
    public ResponseEntity<User> getUser(@PathVariable(value = "id") final Long id){
        final Optional<User> userOptional = getUserUseCase.getUser(id);
        if(userOptional.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(userOptional.get());
    }

    //ADD THIS TO TEST
    @GetMapping("restaurant/{id}")
    @IsAuthenticated
    @RolesAllowed({"ROLE_USER", "ROLE_RESTAURANT", "ROLE_ADMIN"})
    public ResponseEntity<Long> getRestaurantId(@PathVariable(value = "id") final Long id){
        Long restaurantId = getRestaurantIdUseCase.getRestaurantId(id);
        if(restaurantId == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(restaurantId);
    }

    //ADD THIS TO TEST
    @GetMapping("getUsersFromOrder/{restaurantId}")
    @IsAuthenticated
    @RolesAllowed({"ROLE_RESTAURANT", "ROLE_ADMIN"})
    public ResponseEntity<GetUsersResponse> getUsersIfOrdered(@PathVariable(value = "restaurantId") final Long restaurantId) {
        return ResponseEntity.ok(getUsersIfOrderedUseCase.getUsersIfOrdered(restaurantId));

//        final Optional<User> userOptional = getUsersIfOrderedUseCase.getUserIfOrdered(restaurantId, userId);
//        if(userOptional.isEmpty()){
//            return ResponseEntity.notFound().build();
//        }
//        return ResponseEntity.ok().body(userOptional.get());
    }

    @GetMapping()
    @IsAuthenticated
    @RolesAllowed({"ROLE_ADMIN"})
    public ResponseEntity<GetUsersResponse> getUsers(){
        return ResponseEntity.ok(getUsersUseCase.getUsers());
    }

    @PostMapping(value = "/save")
    public ResponseEntity<CreateUserResponse> createUser(@RequestBody @Valid CreateUserRequest request){
        CreateUserResponse response = createUserUseCase.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping(value = "/saveRestaurant")
    public ResponseEntity<CreateUserResponse> createUserRestaurant(@RequestBody @Valid CreateUserRequest request){
        CreateUserResponse response = createUserRestaurantUseCase.createUserRestaurant(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping(value = "/delete/{id}")
    @IsAuthenticated
    @RolesAllowed({"ROLE_USER", "ROLE_RESTAURANT", "ROLE_ADMIN"})
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        deleteUserUseCase.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/update/{id}")
    @IsAuthenticated
    @RolesAllowed({"ROLE_USER", "ROLE_RESTAURANT", "ROLE_ADMIN"})
    public ResponseEntity<Void> updateUser(@PathVariable Long id, @RequestBody @Valid UpdateUserRequest request) {
        request.setId(id);
        updateUserUseCase.updateUser(request);
        return ResponseEntity.noContent().build();
    }
}
