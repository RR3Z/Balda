package ui.utils;

import java.util.Map;

public class GameWidgetUtils {
    public static int MIN_FIELD_SIZE = 2;
    public static int MAX_FIELD_SIZE = 14;

    public static  <K, V> K getKeyByValue(Map<K, V> map, V value) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (value.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }
}
