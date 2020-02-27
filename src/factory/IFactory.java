package factory;

import player.AbstractPlayer;

public interface IFactory {

    /**
     * Creates a Player which implements the strategy given as a parameter.
     *
     * @param strategy The required Strategy.
     * @return a Player which follows the strategy given as a parameter.
     */

     AbstractPlayer createPlayer(Strategy strategy);

}
