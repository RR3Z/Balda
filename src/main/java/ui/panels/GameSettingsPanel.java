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

        _widthSpinner = new JSpinner(new SpinnerNumberModel(5, GameWidgetUtils.MIN_FIELD_SIZE_SPINNER_VALUE, GameWidgetUtils.MAX_FIELD_SIZE_SPINNER_VALUE, 1));
        _heightSpinner = new JSpinner(new SpinnerNumberModel(5, GameWidgetUtils.MIN_FIELD_SIZE_SPINNER_VALUE, GameWidgetUtils.MAX_FIELD_SIZE_SPINNER_VALUE, 1));

        fieldSizesPanel.add(fieldSizesLabel);
        fieldSizesPanel.add(_widthSpinner);
        fieldSizesPanel.add(_heightSpinner);
        // -----------------------------------------

        _widthSpinner.setFont(GameWidgetUtils.font(GameWidgetUtils.OPTION_PANE_FONT_SIZE));
        _heightSpinner.setFont(GameWidgetUtils.font(GameWidgetUtils.OPTION_PANE_FONT_SIZE));
        fieldSizesLabel.setFont(GameWidgetUtils.font(GameWidgetUtils.OPTION_PANE_FONT_SIZE));

        this.add(fieldSizesPanel);
    }

    public int getWidthSpinnerValue() {
        return (int)_widthSpinner.getValue();
    }

    public int getHeightSpinnerValue() {
        return (int)_heightSpinner.getValue();
    }
}
