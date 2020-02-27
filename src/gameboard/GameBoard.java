package gameboard;

import asset.AssetDictionary;
import factory.PlayerFactory;
import factory.Strategy;
import player.AbstractPlayer;
import player.BasicPlayer;
import player.BribePlayer;
import player.GreedyPlayer;

import java.util.List;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class GameBoard {

    private static final int        LEGAL_ASSETS_NR = 4;
    private static Queue<Integer>   deck;
    private static AbstractPlayer[] playerArray;
    private static int              nrOfPlayers;
    private static int              roundCount;
    private static int              maxRounds;
    private static int              currentSherriff;
    private static int[]            scores;
    private static List<Integer>[]  kings = new List[LEGAL_ASSETS_NR];
    private static List<Integer>[]  queens = new List[LEGAL_ASSETS_NR];

    /** Initialises the game board according to the given input.
     *
     * @param strategies list of strategies which the players will follow
     * @param assetDeck list of asset IDs
     */
    public GameBoard(final Strategy[] strategies, final Queue<Integer> assetDeck) {
        deck = assetDeck;
        nrOfPlayers = strategies.length;
        maxRounds = 2 * nrOfPlayers;
        roundCount = 0;
        playerArray = new AbstractPlayer[nrOfPlayers];
        scores = new int[nrOfPlayers];
        for (int i = 0; i < LEGAL_ASSETS_NR; ++i) {
            kings[i] = new LinkedList<>();
            queens[i] = new LinkedList<>();
        }
        for (int i = 0; i < nrOfPlayers; ++i) {
            scores[i] = 0;
        }
        currentSherriff = 0;
        for (int i = 0; i < nrOfPlayers; ++i) {
            playerArray[i] = PlayerFactory.INSTANCE.createPlayer(strategies[i]);
        }
    }

    /** Decides which are the players who will be inspected in the inspection phase.
     *
     * @return list of said players
     */
    private static List<Integer> getInspectedPlayers() {
        List<Integer> ret = new LinkedList<>();
        // if the sheriff follows the Basic Strategy, everybody will be inspected
        if (playerArray[currentSherriff] instanceof BasicPlayer) {
            for (int i = 0; i < nrOfPlayers; ++i) {
                if (i != currentSherriff) {
                    ret.add(i);
                }
            }
        }
        // if the sheriff follows the Greedy Strategy, everybody that didn't bribe
        // the bag will be inspected
        if (playerArray[currentSherriff] instanceof GreedyPlayer) {
            for (int i = 0; i < nrOfPlayers; ++i) {
                if (i != currentSherriff) {
                    if (playerArray[i].bag.hasBribe()) {
                        playerArray[currentSherriff].goldCount
                                += playerArray[i].bag.getBribeCount();
                        playerArray[i].bag.setBribeCount(0);
                    } else {
                        ret.add(i);
                    }
                }
            }
        }
        // if the sheriff follows the Bribe Strategy, players that sit next to him
        // will be inspected
        if (playerArray[currentSherriff] instanceof BribePlayer) {
            ret.add(((currentSherriff - 1) % nrOfPlayers + nrOfPlayers) % nrOfPlayers);
            if (!ret.contains(((currentSherriff + 1) % nrOfPlayers))) {
                ret.add((currentSherriff + 1) % nrOfPlayers);
            }
        }
        return ret;
    }

    /** Adds required number of assets in each player's hand.
     */
    private static void drawPhase() {
        for (int i = 0; i < nrOfPlayers; ++i) {
            playerArray[i].drawFrom(deck);
        }
    }

    /** Sets up the bag for every merchant.
     *
     */
    private static void setupPhase() {
        for (int i = 0; i < nrOfPlayers; ++i) {
            if (i != currentSherriff) {
                playerArray[i].setupBag();
            }
        }
    }

    /** Inspects the bag of required players.
     *
     */
    private static void inspectionPhase() {
        List<Integer> toBeInspected = GameBoard.getInspectedPlayers();
        for (Integer i : toBeInspected) {
            playerArray[i].goldCount += playerArray[i].bag.getBribeCount();
            playerArray[i].bag.setBribeCount(0);
            boolean isHonest = true;
            for (Integer asset : playerArray[i].bag.assetsInBag) {
                if (!asset.equals(playerArray[i].bag.declaredAsset)) {
                    isHonest = false;
                    break;
                }
            }
            if (isHonest) {
                int penalty = playerArray[i].bag.assetsInBag.size()
                              * AssetDictionary.get(playerArray[i].bag.declaredAsset).assetPenalty;
                playerArray[i].goldCount += penalty;
                playerArray[currentSherriff].goldCount -= penalty;
                for (Integer asset : playerArray[i].bag.assetsInBag) {
                    playerArray[i].addOnStand(asset);
                }
            } else {
                int penalty = 0;
                for (Integer asset : playerArray[i].bag.assetsInBag) {
                    if (!asset.equals(playerArray[i].bag.declaredAsset)) {
                        penalty += AssetDictionary.get(asset).assetPenalty;
                        deck.add(asset);
                    } else {
                        playerArray[i].addOnStand(asset);
                    }
                }
                playerArray[i].goldCount -= penalty;
                playerArray[currentSherriff].goldCount += penalty;
            }
        }
        for (int i = 0; i < nrOfPlayers; ++i) {
            if (i != currentSherriff && !toBeInspected.contains(i)) {
                // case in which player [i] put bribe into the bag , the sheriff didn't
                // check for bribe beforehand and [i] didn't get inspected
                playerArray[currentSherriff].goldCount += playerArray[i].bag.getBribeCount();
                playerArray[i].bag.setBribeCount(0);
                for (Integer asset : playerArray[i].bag.assetsInBag) {
                    playerArray[i].addOnStand(asset);
                }
            }
        }
    }

    /** Empties the bags and updates the following.
     *  - current sheriff
     *  - round number
        - Greedy Players' personal round counters
     */
    private static void endRound() {
        for (int i = 0; i < nrOfPlayers; ++i) {
            playerArray[i].bag.clearBag();
            if (playerArray[i] instanceof GreedyPlayer && i != currentSherriff) {
                ((GreedyPlayer) playerArray[i]).updateCounter();
            }
        }
        roundCount++;
        currentSherriff = (currentSherriff + 1) % nrOfPlayers;
    }

    /** Adds the bonuses of Illegal Assets on the players' stands.
     *
     */
    private static void addIllegalBonuses() {
        List<Integer> bonusAssetsList = new LinkedList<>();
        for (int i = 0; i < nrOfPlayers; ++i) {
            bonusAssetsList.clear();
            for (Integer asset : playerArray[i].assetsOnMerchantStand) {
                if (!AssetDictionary.get(asset).assetIsLegal) {
                    String buffer = AssetDictionary.get(asset).assetBonus;
                    int nrOfBonusAssets = Integer.parseInt(buffer.substring(0, 1));
                    Integer bonusAsset = Integer.parseInt(buffer.substring(2));
                    for (int j = 0; j < nrOfBonusAssets; ++j) {
                        bonusAssetsList.add(bonusAsset);
                    }
                }
            }
            for (Integer asset : bonusAssetsList) {
                playerArray[i].addOnStand(asset);
            }
        }
    }

    /** Gets the players which are eligible for King's and Queen's bonuses.
     *
     */
    private static void computeRoyals() {
        Map<Integer, Integer> countAssets = new HashMap<>();
        for (Integer curentAsset = 0; curentAsset < LEGAL_ASSETS_NR; ++curentAsset) {
            Integer maxAssets = 0;
            for (int i = 0; i < nrOfPlayers; ++i) {
                countAssets.put(i, 0);
                for (Integer asset : playerArray[i].assetsOnMerchantStand) {
                    if (asset.equals(curentAsset)) {
                        countAssets.put(i, countAssets.getOrDefault(i, 0) + 1);
                    }
                }
            }
            for (Integer player : countAssets.keySet()) {
                if (maxAssets < countAssets.get(player)) {
                    maxAssets = countAssets.get(player);
                }
            }
            for (Integer player : countAssets.keySet()) {
                if (countAssets.get(player).equals(maxAssets)) {
                    kings[curentAsset].add(player);
                }
            }
            for (Integer player : kings[curentAsset]) {
                countAssets.remove(player);
            }
            maxAssets = 0;
            if (countAssets.keySet().isEmpty()) {
                queens[curentAsset].clear();
                countAssets.clear();
                continue;
            }
            for (Integer player : countAssets.keySet()) {
                if (maxAssets < countAssets.get(player)) {
                    maxAssets = countAssets.get(player);
                }
            }
            for (Integer player : countAssets.keySet()) {
                if (countAssets.get(player).equals(maxAssets)) {
                    queens[curentAsset].add(player);
                }
            }
            countAssets.clear();
        }
    }

    /** Adds the gold count, profit from assets and bonuses to compute each player's
     * final score.
     */
    private static void computeScore() {
        for (int i = 0; i < nrOfPlayers; ++i) {
            int playerScore = playerArray[i].goldCount;
            for (Integer asset : playerArray[i].assetsOnMerchantStand) {
                playerScore += AssetDictionary.get(asset).assetProfit;
            }
            for (int currentAsset = 0; currentAsset < LEGAL_ASSETS_NR; ++currentAsset) {
                if (kings[currentAsset].contains(i)) {
                    playerScore += AssetDictionary.get(currentAsset).assetKingBonus;
                }
                if (queens[currentAsset].contains(i)) {
                    playerScore += AssetDictionary.get(currentAsset).assetQueenBonus;
                }
            }
            scores[i] = playerScore;
        }
    }

    /**Returns a string that is used for printing the output.
     *
     * @param i - index of the player in the playerArray
     * @return a string that describes the player's strategy
     */
    private static String getType(final int i) {
        if (playerArray[i] instanceof GreedyPlayer) {
            return "GREEDY";
        }
        if (playerArray[i] instanceof BasicPlayer) {
            return "BASIC";
        }
        if (playerArray[i] instanceof BribePlayer) {
            return "BRIBED";
        }
        return "INVALID PLAYER";
    }

    /** Prints the scores of each player, sorted from
     * highest to lowest.
     */
    private static void printScores() {
        List<Ranking> rankings = new LinkedList<>();
        ScoreComparator comparator = new ScoreComparator();
        for (int i = 0; i < nrOfPlayers; ++i) {
            rankings.add(new Ranking(getType(i), scores[i]));
        }
        Collections.sort(rankings, comparator);
        for (Ranking r : rankings) {
            System.out.println(r);
        }

    }


    public static void playGame() {
        while (roundCount < maxRounds) {
            drawPhase();
            setupPhase();
            inspectionPhase();
            endRound();
        }
        addIllegalBonuses();
        computeRoyals();
        computeScore();
        printScores();
    }

}
