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

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class RestaurantsControllerTest {
    //ADDED ROW IN TABLE
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GetRestaurantUseCase getRestaurantUseCase;
    @MockBean
    private GetRestaurantsUseCase getRestaurantsUseCase;
    @MockBean
    private CreateRestaurantUseCase createRestaurantUseCase;
    @MockBean
    private DeleteRestaurantUseCase deleteRestaurantUseCase;
    @MockBean
    private UpdateRestaurantUseCase updateRestaurantUseCase;
    @Test
    @WithMockUser(username = "allah@gmail.com", roles = {"RESTAURANT"})
    void getRestaurant_shouldReturn200WithRestaurant_whenRestaurantFound() throws Exception {
        Restaurant restaurant = Restaurant.builder()
                .id(10L)
                .name("Smoked burgers")
                .address("Center")
                .user_id(2L)
                .type("american")
                .build();
        when(getRestaurantUseCase.getRestaurant(10L)).thenReturn(Optional.of(restaurant));

        mockMvc.perform(get("/restaurants/10"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
                .andExpect(content().json("""
                        {"id":10,"name":"Smoked burgers","user_id":2,"type":"american","address":"Center"}
                        """));

        verify(getRestaurantUseCase).getRestaurant(10L);
    }

    @Test
    @WithMockUser(username = "allah@gmail.com", roles = {"RESTAURANT"})
    void getRestaurant_shouldReturn404_whenRestaurantNotFound() throws Exception {
        when(getRestaurantUseCase.getRestaurant(10L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/restaurants/10"))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(getRestaurantUseCase).getRestaurant(10L);
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = {"ADMIN"})
    void getRestaurants_shouldReturn200WithRestaurantsList() throws Exception {
        GetRestaurantsResponse response = GetRestaurantsResponse.builder()
                .restaurants(List.of(
                        Restaurant.builder()
                                .id(10L)
                                .name("Smoked burgers")
                                .address("Center")
                                .user_id(2L)
                                .type("american")
                                .build(),
                        Restaurant.builder()
                                .id(11L)
                                .name("Mustard onions")
                                .address("Center")
                                .user_id(3L)
                                .type("asian")
                                .build()
                ))
                .build();
        when(getRestaurantsUseCase.getRestaurants()).thenReturn(response);

        mockMvc.perform(get("/restaurants"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
                .andExpect(content().json("""
                        {
                            "restaurants":[
                                {"id":10,"name":"Smoked burgers","user_id":2,"type":"american","address":"Center"},
                                {"id":11,"name":"Mustard onions","user_id":3,"type":"asian","address":"Center"}
                            ]
                        }
                        """));

        verify(getRestaurantsUseCase).getRestaurants();
    }

    @Test
    void createRestaurant_shouldReturn201_whenRequestIsValid() throws Exception {
        CreateRestaurantRequest request = CreateRestaurantRequest.builder()
                .name("Smoked burgers")
                .address("Center")
                .type("american")
                .build();
        when(createRestaurantUseCase.createRestaurant(request))
                .thenReturn(CreateRestaurantResponse.builder().restaurantId(250L).build());

        mockMvc.perform(post("/restaurants/save")
                .contentType(APPLICATION_JSON_VALUE)
                .content("""
                        {
                            "name": "Smoked burgers",
                            "type": "american",
                            "address": "Center"
                        }
                        """))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json("""
                        {
                            "restaurantId": 250
                        }
                        """));

        verify(createRestaurantUseCase).createRestaurant(request);
    }

    @Test
    @WithMockUser(username = "allah@gmail.com", roles = {"RESTAURANT"})
    void deleteRestaurant_shouldReturn204() throws Exception {
        mockMvc.perform(delete("/restaurants/103"))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(deleteRestaurantUseCase).deleteRestaurant(103L);
    }

    @Test
    @WithMockUser(username = "allah@gmail.com", roles = {"RESTAURANT"})
    void updateRestaurant_shouldReturn204() throws Exception {
        mockMvc.perform(put("/restaurants/update/103")
                .contentType(APPLICATION_JSON_VALUE)
                .content("""
                        {
                            "id": 103,
                            "name": "Big Smoke",
                            "type": "american",
                            "address": "Center"
                        }
                        """))
                .andDo(print())
                .andExpect(status().isNoContent());

        UpdateRestaurantRequest expectedRequest = UpdateRestaurantRequest.builder()
                .id(103L)
                .name("Big Smoke")
                .type("american")
                .address("Center")
                .build();
        verify(updateRestaurantUseCase).updateRestaurant(expectedRequest);
    }
}