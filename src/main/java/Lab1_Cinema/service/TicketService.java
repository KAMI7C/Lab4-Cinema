package Lab1_Cinema.service;

import Lab1_Cinema.model.Ticket;
import Lab1_Cinema.repository.TicketRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TicketService {

    //логгер для этого класса
    private static final Logger log = LoggerFactory.getLogger(TicketService.class);

    private final TicketRepository repository;

    public TicketService(TicketRepository repository) {
        this.repository = repository;
    }

    public void buyTicket(int sessionId, int seat) {
        // INFO: начало операции
        log.info("Запрос на покупку билета: сеанс={}, место={}", sessionId, seat);

        if (seat < 1 || seat > 20) {
            // ERROR: некорректный ввод (ошибка)
            log.error("Ошибка покупки: место {} не существует", seat);
            throw new IllegalArgumentException("Такого места не существует");
        }

        if (repository.isSeatTaken(sessionId, seat)) {
            // WARN: попытка занять уже занятое место
            log.warn("Попытка купить уже занятое место: сеанс={}, место={}", sessionId, seat);
            throw new IllegalArgumentException("Место уже занято");
        }

        try {
            Ticket ticket = new Ticket(sessionId, seat);
            repository.save(ticket);

            // INFO: успешное завершение операции
            log.info("Билет успешно оформлен: сеанс={}, место={}", sessionId, seat);
        } catch (RuntimeException e) {
            // ERROR: непредвиденная ошибка при сохранении
            log.error("Ошибка при сохранении билета: сеанс={}, место={}", sessionId, seat, e);
            throw e;
        }
    }
}