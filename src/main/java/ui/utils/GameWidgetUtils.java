package ui.utils;

import org.jetbrains.annotations.NotNull;
import ui.enums.BorderType;
import ui.enums.ColorType;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class GameWidgetUtils {
    private final static String RESOURCES_FOLDER = "Resources/";

    public final static int MIN_FIELD_SIZE_SPINNER_VALUE = 2;
    public final static int MAX_FIELD_SIZE_SPINNER_VALUE = 14;

    public final static int OPTION_PANE_FONT_SIZE = 16;

    public static void placeContainerInCenter(@NotNull Container container) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension windowSize = container.getSize();
        container.setBounds(screenSize.width/2 - windowSize.width/2, screenSize.height/2 - windowSize.height/2, windowSize.width + 20, windowSize.height + 20);
    }

    public static Color color(ColorType type) {
        return switch (type) {
            case TRANSPARENT -> new Color(0, 0, 0, 0);
            case DEFAULT_BORDER -> Color.BLACK;
            case HIGHLIGHTED_BORDER -> new Color(98, 138, 5);
            case HIGHLIGHTED_SKIP_TURN_BORDER -> new Color(148, 15, 17);
            case CELL_IN_WORD, CANCEL_ACTION_BUTTON, ACTIVE_ALPHABET_BUTTON, SUBMIT_WORD_BUTTON, ACTIVE_PLAYER -> new Color(167, 201, 87);
            case CHANGED_CELL, SKIP_TURN_BUTTON -> new Color(235, 93, 95);
            case TABLE_HEADER -> new Color(242, 232, 207);
            default -> Color.RED;
        };
    }

    public static int borderThickness(BorderType type) {
        return switch (type) {
            case DEFAULT -> 1;
            case BOLD -> 2;
            case EXTRA_BOLD -> 3;
            default -> 1;
        };
    }

    public static Font font(int fontSize) {
        if(fontSize <= 0) {
            throw new IllegalArgumentException("GameWidgetUtils -> getFont: font size must be greater than 0");
        }

        Font font;

        // Uploading font
        try {
            File fontFile = new File(RESOURCES_FOLDER + "SrirachaCyrillic.ttf");
            font = Font.createFont(Font.TRUETYPE_FONT, new FileInputStream(fontFile)).deriveFont(Font.PLAIN, fontSize);
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
        }
        catch (IOException | FontFormatException e) {
            throw new RuntimeException(e);
        }

        return font;
    }

    public static String htmlFont(int fontSize) {
        if(fontSize <= 0) {
            throw new IllegalArgumentException("GameWidgetUtils -> getHtmlFont: font size must be greater than 0");
        }

        return String.format("<font face=\"%s\" size=\"%d\">",
                GameWidgetUtils.font(fontSize).getFontName(),
                fontSize/4
        );
    }
}
