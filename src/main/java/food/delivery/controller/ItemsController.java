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
@RequestMapping("/items")
@AllArgsConstructor
public class ItemsController {
    private final GetItemUseCase getItemUseCase;
    private final GetItemsUseCase getItemsUseCase;
    private final GetItemsIfUserOrderedUseCase getItemsIfUserOrderedUseCase;
    private final CreateItemUseCase createItemUseCase;
    private final DeleteItemUseCase deleteItemUseCase;
    private final UpdateItemUseCase updateItemUseCase;

    @IsAuthenticated
    @RolesAllowed({"ROLE_RESTAURANT", "ROLE_ADMIN"})
    @GetMapping("{id}")
    public ResponseEntity<Item> getItem(@PathVariable(value = "id") final Long id){
        final Optional<Item> itemOptional = getItemUseCase.getItem(id);
        if(itemOptional.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(itemOptional.get());
    }

    @IsAuthenticated
    @RolesAllowed({"ROLE_USER", "ROLE_RESTAURANT", "ROLE_ADMIN"})
    @GetMapping()
    public ResponseEntity<GetItemsResponse> getItems(@RequestParam(value = "restaurant", required = true) Long restaurant_id){
        GetAllItemsRequest request = new GetAllItemsRequest();
        request.setRestaurant_id(restaurant_id);
        return ResponseEntity.ok(getItemsUseCase.getItems(request));
    }

    @IsAuthenticated
    @RolesAllowed({"ROLE_USER", "ROLE_ADMIN"})
    @GetMapping(value = "/getItemsFromUser")
    public ResponseEntity<GetItemsResponse> getItemsIfUserOrdered(@RequestParam(value = "user", required = true) Long user_id){
        GetAllItemsIfUserOrderedRequest request = new GetAllItemsIfUserOrderedRequest();
        request.setUser_id(user_id);
        return ResponseEntity.ok(getItemsIfUserOrderedUseCase.getItemsIfUserOrdered(request));
    }

    @IsAuthenticated
    @RolesAllowed({"ROLE_RESTAURANT", "ROLE_ADMIN"})
    @PostMapping(value = "/save")
    public ResponseEntity<CreateItemResponse> createItem(@RequestBody @Valid CreateItemRequest request){
        CreateItemResponse response = createItemUseCase.createItem(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @IsAuthenticated
    @RolesAllowed({"ROLE_RESTAURANT", "ROLE_ADMIN"})
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id){
        deleteItemUseCase.deleteItem(id);
        return ResponseEntity.noContent().build();
    }

    @IsAuthenticated
    @RolesAllowed({"ROLE_RESTAURANT", "ROLE_ADMIN"})
    @PutMapping(value = "/update/{id}")
    public ResponseEntity<Void> updateItem(@PathVariable Long id, @RequestBody @Valid UpdateItemRequest request){
        request.setId(id);
        updateItemUseCase.updateItem(request);
        return ResponseEntity.noContent().build();
    }
}
