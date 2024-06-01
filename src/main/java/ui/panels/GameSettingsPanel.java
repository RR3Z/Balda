package ui.panels;

import ui.utils.GameWidgetUtils;
import ui.utils.configs.SpinnerConfig;

import javax.swing.*;
import java.awt.*;

public class GameSettingsPanel extends JPanel {
    private JSpinner _widthSpinner;
    private JSpinner _heightSpinner;
    private JCheckBox _aiPlayerActivity;

    public GameSettingsPanel() {
        this.setLayout(new GridLayout(3, 0));

        // --------- Panel with information ---------
        JPanel informationPanel = new JPanel();
        informationPanel.setLayout(new BoxLayout(informationPanel, BoxLayout.Y_AXIS));

        JLabel minFieldSizeLabel = new JLabel("Минимальная ширина/высота = " + SpinnerConfig.MIN_SPINNER_VALUE_FOR_FIELD_SIZE);
        minFieldSizeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel maxFieldSizeLabel = new JLabel("Максимальная ширина/высота = " + SpinnerConfig.MAX_SPINNER_VALUE_FOR_FIELD_SIZE);
        maxFieldSizeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        informationPanel.add(minFieldSizeLabel);
        informationPanel.add(maxFieldSizeLabel);

        minFieldSizeLabel.setFont(GameWidgetUtils.font(GameWidgetUtils.OPTION_PANE_FONT_SIZE));
        maxFieldSizeLabel.setFont(GameWidgetUtils.font(GameWidgetUtils.OPTION_PANE_FONT_SIZE));
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
        JPanel playerSettingsPanel = new JPanel();

        JLabel aiPlayerLabel = new JLabel("Играть против бота: ");
        _aiPlayerActivity = new JCheckBox();

        playerSettingsPanel.add(aiPlayerLabel);
        playerSettingsPanel.add(_aiPlayerActivity);

        aiPlayerLabel.setFont(GameWidgetUtils.font(GameWidgetUtils.OPTION_PANE_FONT_SIZE));
        // ------------------------------------------

        this.add(informationPanel);
        this.add(fieldSizesPanel);
        this.add(playerSettingsPanel);
    }

    public int widthSpinnerValue() {
        return (int) _widthSpinner.getValue();
    }

    public int heightSpinnerValue() {
        return (int) _heightSpinner.getValue();
    }

    public boolean isAIPlayer() {
        return _aiPlayerActivity.isSelected();
    }
}
