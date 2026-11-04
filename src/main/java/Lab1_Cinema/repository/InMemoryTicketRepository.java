package Lab1_Cinema.repository;

import Lab1_Cinema.model.Ticket;

import java.util.ArrayList;
import java.util.List;

public class InMemoryTicketRepository implements TicketRepository {

    private List<Ticket> tickets = new ArrayList<>();

    @Override
    public void save(Ticket ticket) {
        tickets.add(ticket);
    }

    @Override
    public boolean isSeatTaken(int sessionId, int seat) {
        for (Ticket ticket : tickets) {
            if (ticket.getSessionId() == sessionId && ticket.getSeat() == seat) {
                return true;
            }
        }

        return false;
    }
}
