package cz.mg.crisp.ui;

import cz.mg.annotations.classes.Utility;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Optional;
import cz.mg.crisp.Version;
import cz.mg.crisp.entity.Fragment;
import cz.mg.crisp.entity.metadata.Metadata;

import javax.swing.*;

public @Utility class CrispWindow extends JFrame {
    private static final Version VERSION = new Version(0, 1, 0);
    private static final String NAME = "JMgCrisp";
    private static final String TITLE = NAME + " " + VERSION;
    private static final int DEFAULT_WIDTH = 1366;
    private static final int DEFAULT_HEIGHT = 768;
    private static final int DEFAULT_PROPERTIES_WIDTH = 256;

    private final @Mandatory MainMenu mainMenu;
    private final @Mandatory SplitPanel splitPanel;
    private final @Mandatory ScenePanel scenePanel;
    private final @Mandatory PropertiesList propertiesList;

    public CrispWindow(@Mandatory Metadata metadata) {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setTitle(TITLE);
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setLocationRelativeTo(null);

        mainMenu = new MainMenu(this);
        setJMenuBar(mainMenu);

        scenePanel = new ScenePanel(metadata);
        propertiesList = new PropertiesList(metadata);

        scenePanel.setFragmentSingleSelectListener(this::onFragmentSelected);

        splitPanel = new SplitPanel();
        splitPanel.setLeftComponent(scenePanel);
        splitPanel.setRightComponent(new ScrollPanel(propertiesList));
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

    public @Mandatory PropertiesList getPropertiesList() {
        return propertiesList;
    }

    private void onFragmentSelected(@Optional Fragment fragment) {
        propertiesList.setObject(fragment != null ? fragment.getObject() : null);
    }
}
