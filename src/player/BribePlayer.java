package player;

import asset.AssetDictionary;
import asset.ProfitComparator;

import java.util.Collections;

public final class BribePlayer extends AbstractPlayer {

    private static final int HIGH_BRIBE = 10;
    private static final int LOW_BRIBE = 5;
    private static final int MANY = 3;

    /** Implements the specific strategy of setting up the bag for the Briber Player.
     */
    public void setupBag() {
        boolean onlyHaveLegalAssets = true;
        int nrOfIllegalAssets = 0;
        for (Integer asset : assetsInHand) {
            if (!AssetDictionary.get(asset).assetIsLegal) {
                onlyHaveLegalAssets = false;
                nrOfIllegalAssets++;
            }
        }
        // if he only has legal assets OR if he doesn't have money for bribing
        // he uses the Basic Strategy
        if (onlyHaveLegalAssets || (goldCount < LOW_BRIBE)) {
            super.defaultSetupBag();
        } else {
            // he will add as many illegal goods as he can, according to his
            // money count
            ProfitComparator comparator = new ProfitComparator();
            Collections.sort(assetsInHand, comparator);
            if (goldCount < HIGH_BRIBE || nrOfIllegalAssets < MANY) {
                int assetsToAdd = 2;
                while (!AssetDictionary.get(assetsInHand.get(0)).assetIsLegal && assetsToAdd > 0) {
                    bag.add(assetsInHand.get(0));
                    assetsInHand.remove(0);
                    assetsToAdd--;
                }
                bag.setBribeCount(LOW_BRIBE);
                goldCount -= LOW_BRIBE;
            } else {
                while (!AssetDictionary.get(assetsInHand.get(0)).assetIsLegal && !bag.isFull()) {
                    bag.add(assetsInHand.get(0));
                    assetsInHand.remove(0);
                }
                bag.setBribeCount(HIGH_BRIBE);
                goldCount -= HIGH_BRIBE;
            }
            // he will declare the goods as being Apples
            bag.declareAs(0);
        }
    }

}
