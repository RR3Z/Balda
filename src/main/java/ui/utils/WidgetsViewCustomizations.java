package ui.utils;

import javax.swing.*;
import java.awt.*;

public class WidgetsViewCustomizations {
    public static final int CELL_BUTTON_SIZE = 50;
    public static final int CELL_BUTTON_BORDER_THICKNESS = 1;
    public static final Color STANDART_CELL_BUTTON_BORDER_COLOR = Color.BLACK;
    public static final Color HOVERED_CELL_BUTTON_BORDER_COLOR = Color.ORANGE;
    public static final Color CLICKED_CELL_BUTTON_COLOR = new Color(167, 201, 87);

    public static final int KEYBOARD_ROW_COUNT = 3;
    public static final int KEYBOARD_BUTTON_SIZE = 50;
    public static final int KEYBOARD_BUTTON_BORDER_THICKNESS = 1;
    public static final Color STANDART_KEYBOARD_BUTTON_BORDER_COLOR = Color.BLACK;
    public static final Color HOVERED_KEYBOARD_BUTTON_BORDER_COLOR = Color.ORANGE;
    public static final Color CLICKED_KEYBOARD_BUTTON_COLOR = new Color(167, 201, 87);

    public static class FixedStateButtonModel extends DefaultButtonModel {
        @Override
        public boolean isPressed() {
            return false;
        }

        @Override
        public boolean isRollover() {
            return false;
        }

        @Override
        public void setRollover(boolean b) {
        }
    }
}
