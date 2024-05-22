package ui.panels;

import ui.utils.GameWidgetUtils;

import javax.swing.*;

public class GameSettingsPanel extends JPanel {
    private JSpinner _widthSpinner;
    private JSpinner _heightSpinner;

    public GameSettingsPanel() {
        // --------- Panel with field sizes ---------
        JPanel fieldSizesPanel = new JPanel();

        JLabel fieldSizesLabel = new JLabel("Выберите размеры поля:");

        _widthSpinner = new JSpinner(new SpinnerNumberModel(5, GameWidgetUtils.MIN_SPINNER_VALUE_FOR_FIELD_SIZE, GameWidgetUtils.MAX_SPINNER_VALUE_FOR_FIELD_SIZE, 1));
        _heightSpinner = new JSpinner(new SpinnerNumberModel(5, GameWidgetUtils.MIN_SPINNER_VALUE_FOR_FIELD_SIZE, GameWidgetUtils.MAX_SPINNER_VALUE_FOR_FIELD_SIZE, 1));

        fieldSizesPanel.add(fieldSizesLabel);
        fieldSizesPanel.add(_widthSpinner);
        fieldSizesPanel.add(_heightSpinner);
        // -----------------------------------------

        _widthSpinner.setFont(GameWidgetUtils.font(GameWidgetUtils.OPTION_PANE_FONT_SIZE));
        _heightSpinner.setFont(GameWidgetUtils.font(GameWidgetUtils.OPTION_PANE_FONT_SIZE));
        fieldSizesLabel.setFont(GameWidgetUtils.font(GameWidgetUtils.OPTION_PANE_FONT_SIZE));

        this.add(fieldSizesPanel);
    }

    public int widthSpinnerValue() {
        return (int)_widthSpinner.getValue();
    }

    public int heightSpinnerValue() {
        return (int)_heightSpinner.getValue();
    }
}
