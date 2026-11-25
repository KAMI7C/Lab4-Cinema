package Lab1_Cinema.service;

import Lab1_Cinema.model.Ticket;
import Lab1_Cinema.repository.TicketRepository;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TicketService {

    private static final Logger log = LoggerFactory.getLogger(TicketService.class);

    private final TicketRepository repository;
    private final MeterRegistry registry;

    // Метрики
    private final Counter operationsTotal;
    private final Counter successTotal;
    private final Counter errorTotal;
    private final Counter ticketsSoldTotal; // Индивидуальная метрика (Кинотеатр)

    public TicketService(TicketRepository repository, MeterRegistry registry) {
        this.repository = repository;
        this.registry = registry;

        // Инициализация метрик
        this.operationsTotal = Counter.builder("application_operations_total")
                .description("Общее количество операций")
                .register(registry);
        this.successTotal = Counter.builder("application_operations_success_total")
                .description("Успешные операции")
                .register(registry);
        this.errorTotal = Counter.builder("application_operations_error_total")
                .description("Ошибочные операции")
                .register(registry);
        this.ticketsSoldTotal = Counter.builder("cinema_tickets_sold_total")
                .description("Количество проданных билетов")
                .register(registry);
    }

    public void buyTicket(int sessionId, int seat) {
        operationsTotal.increment(); // Считаем общую операцию
        log.info("Запрос на покупку билета: сеанс={}, место={}", sessionId, seat);

        if (seat < 1 || seat > 20) {
            errorTotal.increment();
            log.error("Ошибка покупки: место {} не существует", seat);
            throw new IllegalArgumentException("Такого места не существует");
        }

        if (repository.isSeatTaken(sessionId, seat)) {
            errorTotal.increment();
            log.warn("Попытка купить уже занятое место: сеанс={}, место={}", sessionId, seat);
            throw new IllegalArgumentException("Место уже занято");
        }

        try {
            Ticket ticket = new Ticket(sessionId, seat);
            repository.save(ticket);

            successTotal.increment();
            ticketsSoldTotal.increment(); // Индивидуальная метрика
            log.info("Билет успешно оформлен: сеанс={}, место={}", sessionId, seat);
        } catch (RuntimeException e) {
            errorTotal.increment();
            log.error("Ошибка при сохранении билета: сеанс={}, место={}", sessionId, seat, e);
            throw e;
        }
    }

    // Геттер для реестра (понадобится для HTTP-сервера)
    public MeterRegistry getRegistry() {
        return registry;
    }
}
