package player;

import asset.AssetBag;
import asset.AssetDictionary;
import asset.ProfitComparator;

import java.util.List;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;

public abstract class AbstractPlayer {

    private static final int STARTING_GOLD = 50;
    private static final int MAX_ASSETS_IN_BAG = 5;
    private static final int MAX_ASSETS_IN_HAND = 6;

    public int              goldCount;
    List<Integer>           assetsInHand;
    public List<Integer>    assetsOnMerchantStand;
    public AssetBag         bag = new AssetBag();

    AbstractPlayer() {
        goldCount = STARTING_GOLD;
        assetsOnMerchantStand = new LinkedList<>();
        assetsInHand = new LinkedList<>();
    }

    /**Setups the bag for inspection.
     - adds assets in bag
     - sets declared asset
     - adds bribe (if necessary)
     specific implementation for each player strategy*/
    public abstract void setupBag();

    /**Setups the bag for inspection according to the Basic Strategy.
     * - is used in subclasses to avoid code
     * duplication */
    void defaultSetupBag() {
        ProfitComparator comparator = new ProfitComparator();
        List<Integer> assetsCopyInHand = new LinkedList<>(assetsInHand);
        Collections.sort(assetsCopyInHand, comparator);
        Integer declaredAsset = 0;
        boolean onlyHaveIllegalAssets = true;
        for (Integer asset : assetsInHand) {
            if (AssetDictionary.get(asset).assetIsLegal) {
                onlyHaveIllegalAssets = false;
                break;
            }
        }

        if (onlyHaveIllegalAssets) {
            bag.add(assetsCopyInHand.get(0));
            assetsInHand.remove(assetsCopyInHand.get(0));
            bag.declareAs(declaredAsset);
            bag.setBribeCount(0);
        }
        if (!onlyHaveIllegalAssets) {
            Map<Integer, Integer> countAssets = new HashMap<>();
            for (Integer asset : assetsCopyInHand) {
                if (AssetDictionary.get(asset).assetIsLegal) {
                    countAssets.put(asset, countAssets.getOrDefault(asset, 0) + 1);
                }
            }
            Integer maxOcc = 0;
            int currentProfitPerAsset = 0;
            for (Integer key : countAssets.keySet()) {
                if (countAssets.get(key) >= maxOcc) {
                    maxOcc = countAssets.get(key);
                    if (AssetDictionary.get(key).assetProfit >= currentProfitPerAsset) {
                        declaredAsset = key;
                        currentProfitPerAsset = AssetDictionary.get(key).assetProfit;
                    }
                }
            }
            for (Integer asset : assetsInHand) {
                if (AssetDictionary.get(asset).assetIsLegal) {
                    if ((countAssets.get(asset).equals(maxOcc)
                            && (AssetDictionary.get(asset).assetProfit == currentProfitPerAsset))) {
                        declaredAsset = asset;
                        break;
                    }
                }
            }
            for (int i = 0; i < maxOcc && i < MAX_ASSETS_IN_BAG; ++i) {
                bag.add(declaredAsset);
                assetsInHand.remove(declaredAsset);
            }
            bag.declareAs(declaredAsset);
            bag.setBribeCount(0);
        }
    }

    /**Adds specified asset onto the player's merchant stand.
     *
     * @param asset - asset which should be added
     *              on the merchant stand
     */
    public void addOnStand(final Integer asset) {
        assetsOnMerchantStand.add(asset);
    }

    /**Draws up to 6 cards into the player's hand.
     *
     * @param deck - deck of cards from which the player
     *             should draw
     */
    public void drawFrom(final Queue<Integer> deck) {
        while (assetsInHand.size() < MAX_ASSETS_IN_HAND && !(deck.isEmpty())) {
            assetsInHand.add(deck.poll());
        }
    }

}
