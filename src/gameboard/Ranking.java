package gameboard;

public final class Ranking {

    private String  pName;
    private Integer pScore;

    Ranking(final String name, final Integer score) {
        pName = name;
        pScore = score;
    }

    Integer getScore() {
        return pScore;
    }

    String getName() {
        return pName;
    }

    void setScore(final Integer score) {
        pScore = score;
    }

    void setName(final String name) {
        pName = name;
    }

    /** Returns a string of following format.
     * @return "NAME: SCORE"
     */
    @Override
    public String toString() {
        return getName() + ": " + pScore.toString();
    }

}
