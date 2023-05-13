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
@RequestMapping("/orders")
@AllArgsConstructor
public class OrdersController {
    private final GetOrderUseCase getOrderUseCase;
    private final GetOrdersUseCase getOrdersUseCase;
    private final GetOrdersByUserIdUseCase getOrdersByUserIdUseCase;
    private final CreateOrderUseCase createOrderUseCase;
    private final DeleteOrderUseCase deleteOrderUseCase;

    @IsAuthenticated
    @RolesAllowed({"ROLE_RESTAURANT", "ROLE_ADMIN"})
    @GetMapping("{id}")
    public ResponseEntity<Order> getOrder(@PathVariable(value = "id") final Long id){
        final Optional<Order> orderOptional = getOrderUseCase.getOrder(id);
        if(orderOptional.isEmpty()){
            return  ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(orderOptional.get());
    }

    @IsAuthenticated
    @RolesAllowed({"ROLE_RESTAURANT", "ROLE_ADMIN"})
    @GetMapping()
    public ResponseEntity<GetOrdersResponse> getOrders(@RequestParam(value = "restaurant", required = true) Long restaurant_id){
        GetAllOrdersRequest request = new GetAllOrdersRequest();
        request.setRestaurant_id(restaurant_id);
        return ResponseEntity.ok(getOrdersUseCase.getOrders(request));
    }

    //ADD TO TEST
    @IsAuthenticated
    @RolesAllowed({"ROLE_USER", "ROLE_ADMIN"})
    @GetMapping(value = "/userOrders")
    public ResponseEntity<GetOrdersResponse> getOrdersByUserId(@RequestParam(value = "user", required = true) Long user_id){
        GetAllOrdersByUserIdRequest request = new GetAllOrdersByUserIdRequest();
        request.setUser_id(user_id);
        return ResponseEntity.ok(getOrdersByUserIdUseCase.getOrdersByUserId(request));
    }

    @IsAuthenticated
    @RolesAllowed({"ROLE_USER", "ROLE_ADMIN"})
    @PostMapping(value = "/save")
    public ResponseEntity<CreateOrderResponse> createOrder(@RequestBody @Valid CreateOrderRequest request){
        CreateOrderResponse response = createOrderUseCase.createOrder(request);
        return  ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @IsAuthenticated
    @RolesAllowed({"ROLE_RESTAURANT", "ROLE_ADMIN"})
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id){
        deleteOrderUseCase.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
}
