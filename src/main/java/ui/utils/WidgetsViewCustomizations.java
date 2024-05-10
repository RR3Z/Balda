package ui.utils;

import javax.swing.*;
import java.awt.*;

public class WidgetsViewCustomizations {
    // CellButton view customizations
    public static final int CELL_BUTTON_SIZE = 45;
    public static final int CELL_BUTTON_BORDER_THICKNESS = 1;
    public static final Color STANDART_CELL_BUTTON_BORDER_COLOR = Color.BLACK;
    public static final Color HOVERED_CELL_BUTTON_BORDER_COLOR = Color.ORANGE;
    public static final Color CLICKED_CELL_BUTTON_COLOR = new Color(167, 201, 87);

    // KeyboardButton view customizations
    public static final int KEYBOARD_ROW_COUNT = 3;
    public static final int KEYBOARD_BUTTON_SIZE = 50;
    public static final int KEYBOARD_BUTTON_BORDER_THICKNESS = 1;
    public static final Color STANDART_KEYBOARD_BUTTON_BORDER_COLOR = Color.BLACK;
    public static final Color HOVERED_KEYBOARD_BUTTON_BORDER_COLOR = Color.ORANGE;
    public static final Color CLICKED_KEYBOARD_BUTTON_COLOR = new Color(167, 201, 87);

    // For CellButton and KeyboardButton
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


    
    // PlayerActionButton view customizations
    public static final int PLAYER_ACTION_BUTTON_WIDTH = 130;
    public static final int PLAYER_ACTION_BUTTON_HEIGHT = 40;
    public static final int PLAYER_ACTION_BUTTON_BORDER_THICKNESS = 1;
    public static final Color STANDART_PLAYER_ACTION_BUTTON_BORDER_COLOR = Color.BLACK;
    public static final Color HOVERED_PLAYER_ACTION_BUTTON_BORDER_COLOR = Color.ORANGE;

    // CancelActionButton view customizations
    public static final Color CANCEL_ACTION_BUTTON_COLOR = new Color(167, 201, 87);

    // SkipTurnButton view customizations
    public static final Color SKIP_TURN_BUTTON_COLOR = new Color(167, 201, 87);

    // SubmitWordButton view customizations
    public static final Color SUBMIT_WORD_BUTTON_COLOR = new Color(167, 201, 87);
}
