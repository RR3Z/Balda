package ui.panels;

import ui.utils.GameWidgetUtils;
import ui.utils.configs.SpinnerConfig;

import javax.swing.*;
import java.awt.*;

public class GameSettingsPanel extends JPanel {
    private JSpinner _widthSpinner;
    private JSpinner _heightSpinner;

    public GameSettingsPanel() {
        this.setLayout(new GridLayout(2, 0));

        // --------- Panel with information ---------
        JPanel informationPanel = new JPanel();

        JLabel messageLabel = new JLabel("Максимальная ширина/высота = " + SpinnerConfig.MAX_SPINNER_VALUE_FOR_FIELD_SIZE);

        informationPanel.add(messageLabel);

        messageLabel.setFont(GameWidgetUtils.font(GameWidgetUtils.OPTION_PANE_FONT_SIZE));
        // ------------------------------------------

        // --------- Panel with field sizes ---------
        JPanel fieldSizesPanel = new JPanel();

        JLabel fieldSizesLabel = new JLabel("Выберите размеры поля (ширина/высота):");
        _widthSpinner = new JSpinner(new SpinnerNumberModel(5, SpinnerConfig.MIN_SPINNER_VALUE_FOR_FIELD_SIZE, SpinnerConfig.MAX_SPINNER_VALUE_FOR_FIELD_SIZE, 1));
        _heightSpinner = new JSpinner(new SpinnerNumberModel(5, SpinnerConfig.MIN_SPINNER_VALUE_FOR_FIELD_SIZE, SpinnerConfig.MAX_SPINNER_VALUE_FOR_FIELD_SIZE, 1));

        fieldSizesPanel.add(fieldSizesLabel);
        fieldSizesPanel.add(_widthSpinner);
        fieldSizesPanel.add(_heightSpinner);

        fieldSizesLabel.setFont(GameWidgetUtils.font(GameWidgetUtils.OPTION_PANE_FONT_SIZE));
        _widthSpinner.setFont(GameWidgetUtils.font(GameWidgetUtils.OPTION_PANE_FONT_SIZE));
        _heightSpinner.setFont(GameWidgetUtils.font(GameWidgetUtils.OPTION_PANE_FONT_SIZE));
        // ------------------------------------------

        // -------- Panel with Bot settings ---------
        // TODO
        // ------------------------------------------

        this.add(informationPanel);
        this.add(fieldSizesPanel);
    }

    public int widthSpinnerValue() {
        return (int) _widthSpinner.getValue();
    }

    public int heightSpinnerValue() {
        return (int) _heightSpinner.getValue();
    }
}
