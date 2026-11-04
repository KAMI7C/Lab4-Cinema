package Lab1_Cinema.model;

public class Ticket {
    private int sessionId;
    private int seat;

    public Ticket(int sessionId, int seat) {
        this.sessionId = sessionId;
        this.seat = seat;
    }

    public int getSessionId() {
        return sessionId;
    }

    public int getSeat() {
        return seat;
    }
}
