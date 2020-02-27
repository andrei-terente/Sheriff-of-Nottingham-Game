package asset;

import java.util.List;
import java.util.LinkedList;

public final class AssetBag {

    private static final int MAX_ASSETS = 5;

    public List<Integer>    assetsInBag;
    public Integer          declaredAsset;
    private int             bribeCount;

    public AssetBag() {
        assetsInBag = new LinkedList<>();
        bribeCount = 0;
        declaredAsset = 0;
    }

    public void add(final Integer asset) {
        assetsInBag.add(asset);
    }

    public void clearBag() {
        assetsInBag.clear();
        declaredAsset = 0;
        bribeCount = 0;
    }

    public boolean hasBribe() {
        return (bribeCount > 0);
    }

    public int getBribeCount() {
        return bribeCount;
    }

    public void setBribeCount(final int bribeCount) {
        this.bribeCount = bribeCount;
    }

    public void declareAs(final Integer declaredAsset) {
        this.declaredAsset = declaredAsset;
    }

    public boolean isFull() {
        return (assetsInBag.size() >= MAX_ASSETS);
    }

}
