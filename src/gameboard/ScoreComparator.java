package gameboard;

import java.util.Comparator;

public final class ScoreComparator implements Comparator<Ranking> {
    @Override
    public int compare(final Ranking i1, final Ranking i2) {
        return i2.getScore() - i1.getScore();
    }
}
