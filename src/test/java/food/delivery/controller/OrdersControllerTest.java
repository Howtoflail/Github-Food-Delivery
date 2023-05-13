package food.delivery.controller;

import food.delivery.business.*;
import food.delivery.domain.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class OrdersControllerTest {
    //ADDED NEW COLUMN IN DB
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private GetOrderUseCase getOrderUseCase;
    @MockBean
    private GetOrdersUseCase getOrdersUseCase;
    @MockBean
    private CreateOrderUseCase createOrderUseCase;
    @MockBean
    private DeleteOrderUseCase deleteOrderUseCase;

    @Test
    @WithMockUser(username = "allah@gmail.com", roles = {"RESTAURANT"})
    void getOrder_shouldReturn200WithOrder_whenOrderFound() throws Exception {
        String date = "2023-01-06T16:29:28.977044+01:00";
        OffsetDateTime dateTime = OffsetDateTime.parse(date);

        Order order = Order.builder()
                .id(3L)
                .user_id(2L)
                .restaurant_id(1L)
                //.dateTime(dateTime)
                .build();
        when(getOrderUseCase.getOrder(order.getId())).thenReturn(Optional.of(order));

        mockMvc.perform(get("/orders/3"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
                .andExpect(content().json("""
                        {"id":3,"user_id":2,"restaurant_id":1}
                        """));

        verify(getOrderUseCase).getOrder(3L);
    }

    @Test
    @WithMockUser(username = "allah@gmail.com", roles = {"RESTAURANT"})
    void getOrder_shouldReturn404Error_whenOrderNotFound() throws Exception {
        when(getOrderUseCase.getOrder(4L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/orders/4"))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(getOrderUseCase).getOrder(4L);
    }

    @Test
    @WithMockUser(username = "allah@gmail.com", roles = {"RESTAURANT"})
    void getOrders_shouldReturn200WithOrdersList() throws Exception {
        GetOrdersResponse response = GetOrdersResponse.builder()
                .orders(List.of(
                        Order.builder().id(1L).user_id(2L).restaurant_id(1L).build(),
                        Order.builder().id(2L).user_id(2L).restaurant_id(1L).build()
                )).build();
        GetAllOrdersRequest request = GetAllOrdersRequest.builder().restaurant_id(1L).build();
        when(getOrdersUseCase.getOrders(request)).thenReturn(response);

        mockMvc.perform(get("/orders").param("restaurant", "1"))
                .andDo(print())
                .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
                .andExpect(content().json("""
                        {
                            "orders": [
                                {"id":1,"user_id":2,"restaurant_id":1},
                                {"id":2,"user_id":2,"restaurant_id":1}
                            ]
                        }
                        """));

        verify(getOrdersUseCase).getOrders(request);
    }

    @Test
    @WithMockUser(username = "allah@gmail.com", roles = {"USER"})
    void createOrder_shouldReturn201_whenRequestIsValid() throws Exception {
        CreateOrderRequest request = CreateOrderRequest.builder()
                .user_id(10L)
                .restaurant_id(2L)
                .build();
        when(createOrderUseCase.createOrder(request))
                .thenReturn(CreateOrderResponse.builder().orderId(103L).build());

        mockMvc.perform(post("/orders/save")
                .contentType(APPLICATION_JSON_VALUE)
                .content("""
                        {
                            "user_id": 10,
                            "restaurant_id": 2
                        }
                        """))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json("""
                            {"orderId": 103}
                        """));

        verify(createOrderUseCase).createOrder(request);
    }

    @Test
    @WithMockUser(username = "allah@gmail.com", roles = {"RESTAURANT"})
    void deleteOrder_shouldReturn204() throws Exception {
        mockMvc.perform(delete("/orders/301"))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(deleteOrderUseCase).deleteOrder(301L);
    }
}