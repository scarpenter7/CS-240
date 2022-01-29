package request;

public class FillGenerationsRequest {
    private int generations;
    private String username;

    public FillGenerationsRequest(String username, int numGenerations)  {
        this.generations = numGenerations;
        this.username = username;
    }

    public int getGenerations() {
        return generations;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
