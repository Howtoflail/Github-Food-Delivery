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
class OrderItemsControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private GetOrderItemUseCase getOrderItemUseCase;
    @MockBean
    private GetOrderItemsUseCase getOrderItemsUseCase;
    @MockBean
    private CreateOrderItemUseCase createOrderItemUseCase;
    @MockBean
    private DeleteOrderItemUseCase deleteOrderItemUseCase;

    @Test
    @WithMockUser(username = "allah@gmail.com", roles = {"RESTAURANT"})
    void getOrderItem_shouldReturn200WithOrderItem_whenOrderItemFound() throws Exception {
        OrderItem orderItem = OrderItem.builder()
                .id(10L)
                .item_id(4L)
                .order_id(2L)
                .build();
        when(getOrderItemUseCase.getOrderItem(orderItem.getId())).thenReturn(Optional.of(orderItem));

        mockMvc.perform(get("/orderitems/10"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
                .andExpect(content().json("""
                            {"id":10,"item_id":4,"order_id":2}
                        """));

        verify(getOrderItemUseCase).getOrderItem(10L);
    }

    @Test
    @WithMockUser(username = "allah@gmail.com", roles = {"RESTAURANT"})
    void getOrderItem_shouldReturn404Error_whenOrderItemNotFound() throws Exception {
        when(getOrderItemUseCase.getOrderItem(15L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/orderitems/15"))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(getOrderItemUseCase).getOrderItem(15L);
    }

    @Test
    @WithMockUser(username = "allah@gmail.com", roles = {"RESTAURANT"})
    void getOrderItems_shouldReturn200WithOrderItemsList() throws Exception {
        GetOrderItemsResponse response = GetOrderItemsResponse.builder()
                .orderItems(List.of(
                        OrderItem.builder().id(1L).item_id(3L).order_id(2L).build(),
                        OrderItem.builder().id(2L).item_id(3L).order_id(2L).build()
                )).build();
        GetAllOrderItemsRequest request = GetAllOrderItemsRequest.builder().restaurant_id(1L).build();
        when(getOrderItemsUseCase.getOrderItems(request)).thenReturn(response);

        mockMvc.perform(get("/orderitems").param("restaurant", "1"))
                .andDo(print())
                .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
                .andExpect(content().json("""
                            {
                                "orderItems": [
                                    {"id":1,"item_id":3,"order_id":2},
                                    {"id":2,"item_id":3,"order_id":2}
                                ]
                            }
                        """));
        verify(getOrderItemsUseCase).getOrderItems(request);
    }

    @Test
    @WithMockUser(username = "allah@gmail.com", roles = {"USER"})
    void createOrderItem_shouldReturn201_whenRequestIsValid() throws Exception {
        CreateOrderItemRequest request = CreateOrderItemRequest.builder()
                .item_id(4L)
                .order_id(2L)
                .build();
        when(createOrderItemUseCase.createOrderItem(request))
                .thenReturn(CreateOrderItemResponse.builder().orderItemId(215L).build());

        mockMvc.perform(post("/orderitems/save")
                .contentType(APPLICATION_JSON_VALUE)
                .content("""
                            {
                                "item_id": 4,
                                "order_id": 2
                            }
                        """))
                .andDo(print())
                .andExpect(content().json("""
                            {"orderItemId": 215}
                        """));

        verify(createOrderItemUseCase).createOrderItem(request);
    }

    @Test
    @WithMockUser(username = "allah@gmail.com", roles = {"RESTAURANT"})
    void deleteOrderItem_shouldReturn204() throws Exception {
        mockMvc.perform(delete("/orderitems/300"))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(deleteOrderItemUseCase).deleteOrderItem(300L);
    }
}