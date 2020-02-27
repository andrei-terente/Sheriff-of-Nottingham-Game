package asset;

import java.util.Comparator;

public final class ProfitComparator implements Comparator<Integer> {
    @Override
    public int compare(final Integer i1, final Integer i2) {
        return AssetDictionary.get(i2).assetProfit - AssetDictionary.get(i1).assetProfit;
    }
}

