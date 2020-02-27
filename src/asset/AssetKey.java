package asset;

public final class AssetKey {

    private final String     assetName;
    public final int        assetProfit;
    public final int        assetPenalty;
    public final boolean    assetIsLegal;
    public final int        assetKingBonus;
    public final int        assetQueenBonus;
    public final String     assetBonus;

    AssetKey(final String name, final int profit, final int penalty, final boolean isLegal,
             final int kingBonus, final int queenBonus, final String bonus) {
        assetName = name;
        assetProfit = profit;
        assetPenalty = penalty;
        assetIsLegal = isLegal;
        assetKingBonus = kingBonus;
        assetQueenBonus = queenBonus;
        assetBonus = bonus;
    }

}
