package food.delivery.business.impl;

import food.delivery.domain.AccessToken;
import food.delivery.persistence.RestaurantRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
//@SpringBootTest

class DeleteRestaurantUseCaseImplTest {
    @Mock
    private RestaurantRepository restaurantRepositoryMock;
    @Mock
    private AccessToken requestAccessToken;
    @InjectMocks
    private DeleteRestaurantUseCaseImpl deleteRestaurantUseCase;

    @Test
    void deleteRestaurant_shouldDeleteRestaurant() {

        deleteRestaurantUseCase.deleteRestaurant(1L);

        verify(restaurantRepositoryMock).deleteById(1L);
    }
}