package Lab1_Cinema;

import Lab1_Cinema.repository.InMemoryTicketRepository;
import Lab1_Cinema.repository.TicketRepository;
import Lab1_Cinema.service.TicketService;

public class Main {
    public static void main(String[] args) {
        TicketRepository repository = new InMemoryTicketRepository();
        TicketService service = new TicketService(repository);

        service.buyTicket(1, 10);
    }
}
