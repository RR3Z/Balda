package ui.utils;

import javax.swing.*;

public class ButtonUtils {
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
}
