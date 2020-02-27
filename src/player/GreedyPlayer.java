package player;

import asset.AssetDictionary;
import asset.ProfitComparator;

import java.util.Collections;

public final class GreedyPlayer extends AbstractPlayer {
    // counter used to determine the number of rounds in which
    // the greedy player was a merchant
    private int selfCounter = 1;

    /** Implements the specific strategy of setting up the bag for the Greedy Player.
     */
    public void setupBag() {
        super.defaultSetupBag();
        if ((selfCounter % 2) == 0) {
            if (!bag.isFull()) {
                ProfitComparator comparator = new ProfitComparator();
                Collections.sort(assetsInHand, comparator);
                if (!AssetDictionary.get(assetsInHand.get(0)).assetIsLegal) {
                    bag.add(assetsInHand.get(0));
                    assetsInHand.remove(assetsInHand.get(0));
                }
            }
        }
    }

    public void updateCounter() {
        selfCounter++;
    }

}
