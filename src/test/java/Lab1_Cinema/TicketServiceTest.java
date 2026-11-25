package Lab1_Cinema;

import Lab1_Cinema.repository.InMemoryTicketRepository;
import Lab1_Cinema.repository.TicketRepository;
import Lab1_Cinema.service.TicketService;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TicketServiceTest {

    private TicketRepository repository;
    private TicketService service;
    private SimpleMeterRegistry registry; // Добавили реестр для тестов

    @BeforeEach
    void setUp() {
        repository = new InMemoryTicketRepository();
        // Создаем простой реестр (он хранит метрики в памяти)
        registry = new SimpleMeterRegistry();
        // Передаем его в сервис
        service = new TicketService(repository, registry);
    }

    @Test
    void SeatIsFree() {
        service.buyTicket(1, 16);
        assertTrue(repository.isSeatTaken(1, 16));

        // Дополнительно можем проверить, что метрика успеха увеличилась
        assertEquals(1.0, registry.get("application_operations_success_total").counter().count());
    }

    @Test
    void WhenSeatDoesNotExist() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.buyTicket(1, 25));

        assertNotNull(exception.getMessage());
        assertTrue(exception.getMessage().contains("не существует"),
                "Ожидалось сообщение о несуществующем месте, получено: " + exception.getMessage());

        // Проверяем, что метрика ошибки увеличилась
        assertEquals(1.0, registry.get("application_operations_error_total").counter().count());
    }

    @Test
    void shouldNotBuyTicketWhenSeatIsAlreadyTaken() {
        service.buyTicket(1, 10); // Первая покупка (успешная)

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.buyTicket(1, 10)); // Вторая (ошибка)

        assertEquals("Место уже занято", exception.getMessage());

        // Проверяем метрики: 2 операции всего, 1 успешная, 1 ошибка
        assertEquals(2.0, registry.get("application_operations_total").counter().count());
        assertEquals(1.0, registry.get("application_operations_success_total").counter().count());
        assertEquals(1.0, registry.get("application_operations_error_total").counter().count());
    }

    @Test
    void shouldSuccessfullyBuyTicketOnAnotherSeat() {
        // Покупаем билет на сессию 2, место 5
        service.buyTicket(2, 5);

        // Проверяем, что место теперь действительно занято
        assertTrue(repository.isSeatTaken(2, 5));

        // Проверяем индивидуальную метрику (проданные билеты)
        assertEquals(1.0, registry.get("cinema_tickets_sold_total").counter().count());
    }
}