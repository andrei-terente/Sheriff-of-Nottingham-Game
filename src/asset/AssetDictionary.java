package asset;

import java.util.Hashtable;
import java.util.Map;
import java.util.Collections;

public class AssetDictionary {

    private static final int APPLE_ID = 0;
    private static final int APPLE_PROFIT = 2;
    private static final int APPLE_PENALTY = 2;
    private static final int APPLE_K_BONUS = 20;
    private static final int APPLE_Q_BONUS = 10;

    private static final int CHEESE_ID = 1;
    private static final int CHEESE_PROFIT = 3;
    private static final int CHEESE_PENALTY = 2;
    private static final int CHEESE_K_BONUS = 15;
    private static final int CHEESE_Q_BONUS = 10;

    private static final int BREAD_ID = 2;
    private static final int BREAD_PROFIT = 4;
    private static final int BREAD_PENALTY = 2;
    private static final int BREAD_K_BONUS = 15;
    private static final int BREAD_Q_BONUS = 10;

    private static final int CHICKEN_ID = 3;
    private static final int CHICKEN_PROFIT = 4;
    private static final int CHICKEN_PENALTY = 2;
    private static final int CHICKEN_K_BONUS = 10;
    private static final int CHICKEN_Q_BONUS = 5;

    private static final int SILK_ID = 10;
    private static final int SILK_PROFIT = 9;
    private static final int SILK_PENALTY = 4;

    private static final int PEPPER_ID = 11;
    private static final int PEPPER_PROFIT = 8;
    private static final int PEPPER_PENALTY = 4;

    private static final int BARREL_ID = 12;
    private static final int BARREL_PROFIT = 7;
    private static final int BARREL_PENALTY = 4;


    private static final Map<Integer, AssetKey> DICTIONARY;
    static {
        Hashtable<Integer, AssetKey> tmp = new Hashtable<>();
        tmp.put(APPLE_ID, new AssetKey("Apple", APPLE_PROFIT, APPLE_PENALTY,
                true, APPLE_K_BONUS, APPLE_Q_BONUS, "Nothing"));
        tmp.put(CHEESE_ID, new AssetKey("Cheese", CHEESE_PROFIT, CHEESE_PENALTY,
                true, CHEESE_K_BONUS, CHEESE_Q_BONUS, "Nothing"));
        tmp.put(BREAD_ID, new AssetKey("Bread", BREAD_PROFIT, BREAD_PENALTY,
                true, BREAD_K_BONUS, BREAD_Q_BONUS, "Nothing"));
        tmp.put(CHICKEN_ID, new AssetKey("Chicken", CHICKEN_PROFIT, CHICKEN_PENALTY,
                true, CHICKEN_K_BONUS, CHICKEN_Q_BONUS, "Nothing"));
        tmp.put(SILK_ID, new AssetKey("Silk", SILK_PROFIT, SILK_PENALTY,
                false, 0, 0, "3*1"));
        tmp.put(PEPPER_ID, new AssetKey("Pepper", PEPPER_PROFIT, PEPPER_PENALTY,
                false, 0, 0, "2*3"));
        tmp.put(BARREL_ID, new AssetKey("Barrel", BARREL_PROFIT, BARREL_PENALTY,
                false, 0, 0, "2*2"));
        DICTIONARY = Collections.unmodifiableMap(tmp);
    }

    public static AssetKey get(final int id) {
        return DICTIONARY.get(id);
    }

}
