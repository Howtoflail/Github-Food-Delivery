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
class ItemsControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private GetItemUseCase getItemUseCase;
    @MockBean
    private GetItemsUseCase getItemsUseCase;
    @MockBean
    private CreateItemUseCase createItemUseCase;
    @MockBean
    private UpdateItemUseCase updateItemUseCase;
    @MockBean
    private DeleteItemUseCase deleteItemUseCase;

    @Test
    @WithMockUser(username = "allah@gmail.com", roles = {"RESTAURANT"})
    void getItem_shouldReturn200WithItem_whenItemFound() throws Exception {
        Item item = Item.builder()
                .id(3L)
                .name("Bacon")
                .price(20.3)
                .restaurant_id(2L)
                .build();
        when(getItemUseCase.getItem(item.getId())).thenReturn(Optional.of(item));

        mockMvc.perform(get("/items/3"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
                .andExpect(content().json("""
                        {"id":3,"name":"Bacon","price":20.3,"restaurant_id":2}
                        """));

        verify(getItemUseCase).getItem(3L);
    }

    @Test
    @WithMockUser(username = "allah@gmail.com", roles = {"RESTAURANT"})
    void getItem_shouldReturn404Error_whenItemNotFound() throws Exception {
        when(getItemUseCase.getItem(3L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/items/3"))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(getItemUseCase).getItem(3L);
    }

    @Test
    @WithMockUser(username = "allah@gmail.com", roles = {"RESTAURANT"})
    void getItems_shouldReturn200WithItemsList() throws Exception {
        GetItemsResponse response = GetItemsResponse.builder()
                .items(List.of(
                        Item.builder().id(1L).name("Bacon").price(2.3).restaurant_id(3L).build(),
                        Item.builder().id(2L).name("Cheese").price(2.0).restaurant_id(3L).build()
                )).build();
        GetAllItemsRequest request = GetAllItemsRequest.builder().restaurant_id(1L).build();
        when(getItemsUseCase.getItems(request)).thenReturn(response);

        mockMvc.perform(get("/items").param("restaurant", "1"))
                .andDo(print())
                .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
                .andExpect(content().json("""
                        {
                            "items":[
                                {"id":1,"name":"Bacon","price":2.3,"restaurant_id":3},
                                {"id":2,"name":"Cheese","price":2.0,"restaurant_id":3}
                            ]
                        }
                        """));
        verify(getItemsUseCase).getItems(request);
    }

    @Test
    @WithMockUser(username = "allah@gmail.com", roles = {"RESTAURANT"})
    void createItem_shouldReturn201_whenRequestIsValid() throws Exception {
        CreateItemRequest request = CreateItemRequest.builder()
                .name("Mushrooms")
                .price(3.4)
                .restaurant_id(3L)
                .build();
        when(createItemUseCase.createItem(request))
                .thenReturn(CreateItemResponse.builder().itemId(5L).build());

        mockMvc.perform(post("/items/save")
                .contentType(APPLICATION_JSON_VALUE)
                .content("""
                        {
                            "name": "Mushrooms",
                            "price": 3.4,
                            "restaurant_id": 3
                        }
                        """))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json("""
                            {"itemId": 5}
                        """));

        verify(createItemUseCase).createItem(request);
    }

    @Test
    @WithMockUser(username = "allah@gmail.com", roles = {"RESTAURANT"})
    void deleteItem_shouldReturn204() throws Exception {
        mockMvc.perform(delete("/items/109"))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(deleteItemUseCase).deleteItem(109L);
    }

    @Test
    @WithMockUser(username = "allah@gmail.com", roles = {"RESTAURANT"})
    void updateItem_shouldReturn204() throws Exception {
        mockMvc.perform(put("/items/update/109")
                .contentType(APPLICATION_JSON_VALUE)
                .content("""
                            {
                                "name": "Ketchup",
                                "price": "0.9",
                                "restaurant_id": 3
                            }
                        """))
                .andDo(print())
                .andExpect(status().isNoContent());

        UpdateItemRequest expectedRequest = UpdateItemRequest.builder()
                .id(109L)
                .name("Ketchup")
                .price(0.9)
                .restaurant_id(3L)
                .build();

        verify(updateItemUseCase).updateItem(expectedRequest);
    }
}