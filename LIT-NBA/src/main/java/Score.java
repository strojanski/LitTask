public class Score {

    public String date;
    public String score;

    public Score(String date, String score) {
        this.date = date;
        this.score = score;
    }

    public String getDate() {
        return this.date;
    }

    public String getScore() {
        return this.score;
    }

    @Override
    public String toString() {
        return String.format("%s %s", this.date, this.score);
    }
}
