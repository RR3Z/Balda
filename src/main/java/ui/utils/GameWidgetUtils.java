package ui.utils;

import ui.enums.ColorType;

import java.awt.*;
import java.util.Map;

public class GameWidgetUtils {
    public final static int MIN_FIELD_SIZE = 2;
    public final static int MAX_FIELD_SIZE = 14;

    public static  <K, V> K getKeyByValue(Map<K, V> map, V value) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (value.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

    public static Color getColor(ColorType type) {
        return switch (type) {
            case TRANSPARENT -> new Color(0, 0, 0, 0);
            case BACKGROUND -> new Color(250, 237,205, 80);
            case DEFAULT_BORDER -> Color.BLACK;
            case HOVERED_BORDER -> Color.ORANGE;
            case CELL_IN_WORD -> new Color(167, 201, 87);
            case CHANGED_CELL -> new Color(188, 71, 73);
            case INACTIVE_CELL -> Color.WHITE;
            case CANCEL_ACTION_BUTTON -> new Color(167, 201, 87);
            case SKIP_TURN_BUTTON -> new Color(235, 93, 95);
            case SUBMIT_WORD_BUTTON -> new Color(167, 201, 87);
            case ACTIVE_KEYBOARD_BUTTON -> new Color(167, 201, 87);
            case INACTIVE_KEYBOARD_BUTTON -> Color.WHITE;
            default -> Color.RED;
        };
    }

    public static Font getFont() {
        // Здесь подгружать фон из ресурсов
        return null;
    }
}
