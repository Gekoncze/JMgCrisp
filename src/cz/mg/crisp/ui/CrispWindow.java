package cz.mg.crisp.ui;

import cz.mg.annotations.classes.Utility;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.c.CLibrary;

import javax.swing.*;

public @Utility class CrispWindow extends JFrame {
    private static final int DEFAULT_WIDTH = 1366;
    private static final int DEFAULT_HEIGHT = 768;
    private static final int DEFAULT_PROPERTIES_WIDTH = 256;

    private final @Mandatory MainMenu mainMenu;
    private final @Mandatory SplitPanel splitPanel;
    private final @Mandatory ScenePanel scenePanel;
    private final @Mandatory PropertiesPanel propertiesPanel;

    public CrispWindow() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setTitle("JMgCrisp");
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setLocationRelativeTo(null);

        mainMenu = new MainMenu(this);
        setJMenuBar(mainMenu);

        scenePanel = new ScenePanel();
        propertiesPanel = new PropertiesPanel();

        splitPanel = new SplitPanel();
        splitPanel.setLeftComponent(scenePanel);
        splitPanel.setRightComponent(new ScrollPanel(propertiesPanel));
        splitPanel.setDividerLocation(DEFAULT_WIDTH - DEFAULT_PROPERTIES_WIDTH);

        getContentPane().add(splitPanel);
    }

    public @Mandatory MainMenu getMainMenu() {
        return mainMenu;
    }

    public @Mandatory SplitPanel getSplitPanel() {
        return splitPanel;
    }

    public @Mandatory ScenePanel getScenePanel() {
        return scenePanel;
    }

    public @Mandatory PropertiesPanel getPropertiesPanel() {
        return propertiesPanel;
    }

    public static void main(String[] args) {
        CLibrary.load();
        new CrispWindow().setVisible(true);
    }
}
