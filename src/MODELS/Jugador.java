package MODELS;

public class Jugador {
    private int id;
    private String nickname;
    private int wins;
    private int lose;
    private int tie;

    public Jugador(int id, String nickname, int wins, int lose, int tie) {
        this.id = id;
        this.nickname = nickname;
        this.wins = wins;
        this.lose = lose;
        this.tie = tie;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getLose() {
        return lose;
    }

    public void setLose(int lose) {
        this.lose = lose;
    }

    public int getTie() {
        return tie;
    }

    public void setTie(int tie) {
        this.tie = tie;
    }
}
