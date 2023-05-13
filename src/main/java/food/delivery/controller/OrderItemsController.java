package food.delivery.controller;

import food.delivery.business.CreateOrderItemUseCase;
import food.delivery.business.DeleteOrderItemUseCase;
import food.delivery.business.GetOrderItemUseCase;
import food.delivery.business.GetOrderItemsUseCase;
import food.delivery.business.impl.GetOrderItemsIfUserOrderedUseCaseImpl;
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
@RequestMapping("/orderitems")
@AllArgsConstructor
public class OrderItemsController {
    private GetOrderItemUseCase getOrderItemUseCase;
    private GetOrderItemsUseCase getOrderItemsUseCase;
    private GetOrderItemsIfUserOrderedUseCaseImpl getOrderItemsIfUserOrderedUseCase;
    private CreateOrderItemUseCase createOrderItemUseCase;
    private DeleteOrderItemUseCase deleteOrderItemUseCase;

    @IsAuthenticated
    @RolesAllowed({"ROLE_RESTAURANT", "ROLE_ADMIN"})
    @GetMapping("{id}")
    public ResponseEntity<OrderItem> getOrderItem(@PathVariable(value = "id") final Long id) {
        final Optional<OrderItem> orderItemOptional = getOrderItemUseCase.getOrderItem(id);
        if(orderItemOptional.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(orderItemOptional.get());
    }

    @IsAuthenticated
    @RolesAllowed({"ROLE_RESTAURANT", "ROLE_ADMIN"})
    @GetMapping()
    public ResponseEntity<GetOrderItemsResponse> getOrderItems(@RequestParam(value = "restaurant", required = true) Long restaurant_id){
        GetAllOrderItemsRequest request = new GetAllOrderItemsRequest();
        request.setRestaurant_id(restaurant_id);
        return ResponseEntity.ok(getOrderItemsUseCase.getOrderItems(request));
    }

    @IsAuthenticated
    @RolesAllowed({"ROLE_USER", "ROLE_ADMIN"})
    @GetMapping(value = "/getOrderItemsFromUser")
    public ResponseEntity<GetOrderItemsResponse> getOrderItemsIfUserOrdered(@RequestParam(value = "user", required = true) Long user_id){
        GetAllOrderItemsIfUserOrderedRequest request = new GetAllOrderItemsIfUserOrderedRequest();
        request.setUser_id(user_id);
        return ResponseEntity.ok(getOrderItemsIfUserOrderedUseCase.getOrderItemsIfUserOrdered(request));
    }

    //dont allow restaurant to create order item
    @IsAuthenticated
    @RolesAllowed({"ROLE_USER", "ROLE_ADMIN"})
    @PostMapping(value = "/save")
    public ResponseEntity<CreateOrderItemResponse> createOrderItem(@RequestBody @Valid CreateOrderItemRequest request){
        CreateOrderItemResponse response = createOrderItemUseCase.createOrderItem(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @IsAuthenticated
    @RolesAllowed({"ROLE_RESTAURANT", "ROLE_ADMIN"})
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteOrderItem(@PathVariable Long id){
        deleteOrderItemUseCase.deleteOrderItem(id);
        return ResponseEntity.noContent().build();
    }
}
