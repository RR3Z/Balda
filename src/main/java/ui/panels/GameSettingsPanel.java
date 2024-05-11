package ui.panels;

import ui.utils.GameWidgetUtils;

import javax.swing.*;

public class GameSettingsPanel extends JPanel {
    private JSpinner _widthSpinner;
    private JSpinner _heightSpinner;

    public GameSettingsPanel() {
        // --------- Панель с размерами поля ---------
        JPanel fieldSizesPanel = new JPanel();

        JLabel fieldSizesLabel = new JLabel("Выберите размеры поля:");

        _widthSpinner = new JSpinner(new SpinnerNumberModel(5, GameWidgetUtils.MIN_FIELD_SIZE_SPINNER_VALUE, GameWidgetUtils.MAX_FIELD_SIZE_SPINNER_VALUE, 1));
        _heightSpinner = new JSpinner(new SpinnerNumberModel(5, GameWidgetUtils.MIN_FIELD_SIZE_SPINNER_VALUE, GameWidgetUtils.MAX_FIELD_SIZE_SPINNER_VALUE, 1));

        fieldSizesPanel.add(fieldSizesLabel);
        fieldSizesPanel.add(_widthSpinner);
        fieldSizesPanel.add(_heightSpinner);
        // -----------------------------------------

        this.add(fieldSizesPanel);
    }

    public int getWidthSpinnerValue() {
        return (int)_widthSpinner.getValue();
    }

    public int getHeightSpinnerValue() {
        return (int)_heightSpinner.getValue();
    }
}
