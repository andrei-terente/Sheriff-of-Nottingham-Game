package factory;
import player.AbstractPlayer;
import player.BasicPlayer;
import player.GreedyPlayer;
import player.BribePlayer;
import player.RoyalPlayer;

public class PlayerFactory implements IFactory {

    public static final PlayerFactory INSTANCE = new PlayerFactory();

    /** Returns a player of specified strategy;
     *
     * @param strategy The required Strategy.
     * @return player which follows the required
     *         strategy
     */
    public AbstractPlayer createPlayer(final Strategy strategy) {
        switch (strategy) {
            case BASIC:
                return new BasicPlayer();
            case BRIBE:
                return new BribePlayer();
            case GREEDY:
                return new GreedyPlayer();
            case ROYAL:
                return new RoyalPlayer();
            default:
                return new BasicPlayer();
        }
    }

}
