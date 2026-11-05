package Lab1_Cinema;

import Lab1_Cinema.repository.InMemoryTicketRepository;
import Lab1_Cinema.repository.TicketRepository;
import Lab1_Cinema.service.TicketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TicketServiceTest {

    private TicketRepository repository;
    private TicketService service;

    @BeforeEach
    void setUp() {
        repository = new InMemoryTicketRepository();
        service = new TicketService(repository);
    }

    @Test
    void SeatIsFree() {
        service.buyTicket(1, 16);
        assertTrue(repository.isSeatTaken(1, 16));
    }

    @Test
    void WhenSeatDoesNotExist() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.buyTicket(1, 25));

        assertNotNull(exception.getMessage());
        assertTrue(exception.getMessage().contains("не существует"),
                "Ожидалось сообщение о несуществующем месте, получено: " + exception.getMessage());
    }

    @Test
    void shouldNotBuyTicketWhenSeatIsAlreadyTaken() {
        service.buyTicket(1, 10);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.buyTicket(1, 10));

        assertEquals("Места няма2", exception.getMessage());
    }

    @Test
    void shouldSuccessfullyBuyTicketOnAnotherSeat() {
        // Покупаем билет на сессию 2, место 5
        service.buyTicket(2, 5);

        // Проверяем, что место теперь действительно занято
        assertTrue(repository.isSeatTaken(2, 5));
    }

}
