package food.delivery.business.impl;

import food.delivery.domain.AccessToken;
import food.delivery.persistence.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class DeleteOrderUseCaseImplTest {
    @Mock
    private OrderRepository orderRepositoryMock;
    @Mock
    private AccessToken requestAccessToken;
    @InjectMocks
    private DeleteOrderUseCaseImpl deleteOrderUseCase;

    @Test
    void deleteOrder_shouldDeleteOrder() {
        deleteOrderUseCase.deleteOrder(1L);

        verify(orderRepositoryMock).deleteById(1L);
    }
}