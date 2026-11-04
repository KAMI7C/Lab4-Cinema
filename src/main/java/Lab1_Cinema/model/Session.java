package Lab1_Cinema.model;

public class Session {
    private int id;
    private Movie movie;

    public Session(int id, Movie movie) {
        this.id = id;
        this.movie = movie;
    }

    public int getId() {
        return id;
    }

    public Movie getMovie() {
        return movie;
    }
}
