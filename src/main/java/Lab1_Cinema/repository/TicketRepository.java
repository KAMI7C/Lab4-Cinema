package Lab1_Cinema.repository;

import Lab1_Cinema.model.Ticket;

public interface TicketRepository {
    void save(Ticket ticket);
    boolean isSeatTaken(int sessionId, int seat);
}
