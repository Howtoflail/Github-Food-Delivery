package food.delivery.controller;

import com.github.dockerjava.api.exception.BadRequestException;
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
import org.springframework.web.client.HttpClientErrorException;

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
class UsersControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private GetUsersUseCase getUsersUseCase;
    @MockBean
    private GetUserUseCase getUserUseCase;
    @MockBean
    private CreateUserUseCase createUserUseCase;
    @MockBean
    private CreateUserRestaurantUseCase createUserRestaurantUseCase;
    @MockBean
    private DeleteUserUseCase deleteUserUseCase;
    @MockBean
    private UpdateUserUseCase updateUserUseCase;

    @Test
    @WithMockUser(username = "allah@gmail.com", roles = {"USER"})
    void getUser_shouldReturn200WithUser_whenUserFound() throws Exception{
        User user = User.builder()
                .id(4L)
                .first_name("Allah")
                .last_name("Binladen")
                .email("allah@gmail.com")
                .address("Center")
                .phone("+40736756631")
                .password("Coaiemari123")
                .build();
        when(getUserUseCase.getUser(user.getId())).thenReturn(Optional.of(user));

        mockMvc.perform(get("/users/4"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
                .andExpect(content().json("""
                        {"id":4,"first_name":"Allah","last_name":"Binladen","email":"allah@gmail.com","address":"Center","phone":"+40736756631","password":"Coaiemari123"}
                        """));

        verify(getUserUseCase).getUser(4L);
    }

    @Test
    @WithMockUser(username = "allah@gmail.com", roles = {"USER"})
    void getUser_shouldReturn404Error_whenStudentNotFound() throws Exception {
        when(getUserUseCase.getUser(4L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/users/4"))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(getUserUseCase).getUser(4L);
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = {"ADMIN"})
    void getUsers_shouldReturn200WithUsersList() throws Exception {
        GetUsersResponse response = GetUsersResponse.builder()
                .users(List.of(
                        User.builder().id(1L).first_name("Alex").last_name("Big").email("alexbig@gmail.com").address("Cold Russia")
                                .phone("+40736756631").password("Alexbig123").build(),
                        User.builder().id(2L).first_name("Marcel").last_name("Baron").email("marcelbaron@gmail.com")
                                .address("Emmen").phone("+40736756631").password("Marcelbaron123").build()
                )).build();
        when(getUsersUseCase.getUsers()).thenReturn(response);

        mockMvc.perform(get("/users"))
                .andDo(print())
                .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
                .andExpect(content().json("""
                        {
                            "users":[
                                {"id":1,"first_name":"Alex","last_name":"Big","email":"alexbig@gmail.com","address":"Cold Russia","phone":"+40736756631","password":"Alexbig123"},
                                {"id":2,"first_name":"Marcel","last_name":"Baron","email":"marcelbaron@gmail.com","address":"Emmen","phone":"+40736756631","password":"Marcelbaron123"}
                            ]
                        }
                        """));
        verify(getUsersUseCase).getUsers();
    }

    @Test
    void createUser_shouldReturn201_whenRequestIsValid() throws Exception{
        CreateUserRequest request = CreateUserRequest.builder()
                .first_name("Alex")
                .last_name("Big")
                .email("alexbig@gmail.com")
                .address("Cold Russia")
                .phone("+40736756631")
                .password("Alexbig123")
                .build();
        when(createUserUseCase.createUser(request))
                .thenReturn(CreateUserResponse.builder().userId(250L).build());

        mockMvc.perform(post("/users/save")
                .contentType(APPLICATION_JSON_VALUE)
                .content("""
                        {
                            "first_name": "Alex",
                            "last_name" : "Big",
                            "email": "alexbig@gmail.com",
                            "address": "Cold Russia",
                            "phone": "+40736756631",
                            "password": "Alexbig123"
                        }
                        """))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json("""
                            {"userId": 250}
                        """));

        verify(createUserUseCase).createUser(request);
    }

//    @Test
//    void createUser_shouldReturn400_whenRequestIsInvalid() throws Exception {
//        CreateUserRequest request = CreateUserRequest.builder()
//                .first_name("Alex")
//                .last_name("Big")
//                .email("alexbig@gmail.com")
//                .address("Cold Russia")
//                .phone("+40736756631")
//                .build();
////        doThrow(BadRequestException.class)
////                .when(createUserUseCase.createUser(request));
//        when(createUserUseCase.createUser(request))
//                //.thenThrow(BadRequestException.class);
//                .thenReturn(CreateUserResponse.builder().userId(250L).build());
//
//        mockMvc.perform(post("/users/save")
//                        .contentType(APPLICATION_JSON_VALUE)
//                        .content("""
//                        {
//                            "first_name": "Alex",
//                            "last_name" : "Big",
//                            "email": "alexbig@gmail.com",
//                            "address": "Cold Russia",
//                            "phone": "+40736756631"
//                        }
//                        """))
//                .andDo(print())
//                .andExpect(status().isBadRequest());
//
//        //verify(createUserUseCase).createUser(request);
//    }

    @Test
    void createUserRestaurant_shouldReturn201_whenRequestIsValid() throws Exception{
        CreateUserRequest request = CreateUserRequest.builder()
                .first_name("Alex")
                .last_name("Big")
                .email("alexbig@gmail.com")
                .address("Cold Russia")
                .phone("+40736756631")
                .password("Alexbig123")
                .build();
        when(createUserRestaurantUseCase.createUserRestaurant(request))
                .thenReturn(CreateUserResponse.builder().userId(250L).build());

        mockMvc.perform(post("/users/saveRestaurant")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content("""
                        {
                            "first_name": "Alex",
                            "last_name" : "Big",
                            "email": "alexbig@gmail.com",
                            "address": "Cold Russia",
                            "phone": "+40736756631",
                            "password": "Alexbig123"
                        }
                        """))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json("""
                            {"userId": 250}
                        """));

        verify(createUserRestaurantUseCase).createUserRestaurant(request);
    }

    @Test
    @WithMockUser(username = "allah@gmail.com", roles = {"RESTAURANT"})
    void deleteUser_shouldReturn204() throws Exception {
        mockMvc.perform(delete("/users/delete/115"))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(deleteUserUseCase).deleteUser(115L);
    }

    @Test
    @WithMockUser(username = "allah@gmail.com", roles = {"USER"})
    void updateUser_shouldReturn204() throws Exception {
        mockMvc.perform(put("/users/update/110")
                .contentType(APPLICATION_JSON_VALUE)
                .content("""
                        {
                            "first_name": "Alex",
                            "last_name" : "Big",
                            "email": "alexbig@gmail.com",
                            "address": "Cold Russia",
                            "phone": "+40736756631",
                            "password": "Alexbig123"
                        }
                        """))
                .andDo(print())
                .andExpect(status().isNoContent());

        UpdateUserRequest expectedRequest = UpdateUserRequest.builder()
                .id(110L)
                .first_name("Alex")
                .last_name("Big")
                .email("alexbig@gmail.com")
                .address("Cold Russia")
                .phone("+40736756631")
                .password("Alexbig123")
                .build();
        verify(updateUserUseCase).updateUser(expectedRequest);
    }
}