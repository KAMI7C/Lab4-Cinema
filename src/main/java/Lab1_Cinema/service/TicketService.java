package Lab1_Cinema.service;

import Lab1_Cinema.model.Ticket;
import Lab1_Cinema.repository.TicketRepository;

public class TicketService {

    private final TicketRepository repository;

    public TicketService(TicketRepository repository) {
        this.repository = repository;
    }

    public void buyTicket(int sessionId, int seat) {
        System.out.println("Проверяем место: " + seat);

        if (seat < 1 || seat > 20) {
            System.out.println("Место не существует");
            throw new IllegalArgumentException("Такого места не существует");
        }

        System.out.println("Проверяем, занято ли место");

        if (repository.isSeatTaken(sessionId, seat)) {
            System.out.println("Место уже занято");
            throw new IllegalArgumentException("Места няма");
        }

        Ticket ticket = new Ticket(sessionId, seat);
        repository.save(ticket);

        System.out.println("Место свободно — билет куплен");
    }
}
