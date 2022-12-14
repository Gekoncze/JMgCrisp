package cz.mg.crisp.ui;

import cz.mg.annotations.classes.Utility;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Optional;
import cz.mg.crisp.Version;
import cz.mg.crisp.entity.model.Fragment;
import cz.mg.crisp.entity.metadata.Metadata;
import cz.mg.crisp.entity.model.Reference;
import cz.mg.crisp.entity.model.Row;
import cz.mg.crisp.services.OpenService;

import javax.swing.*;

public @Utility class CrispWindow extends JFrame {
    private static final Version VERSION = new Version(0, 1, 0);
    private static final String NAME = "JMgCrisp";
    private static final String TITLE = NAME + " " + VERSION;
    private static final int DEFAULT_WIDTH = 1366;
    private static final int DEFAULT_HEIGHT = 768;
    private static final int DEFAULT_PROPERTIES_WIDTH = 256;

    private final @Mandatory OpenService openService = OpenService.getInstance();

    private final @Mandatory Metadata metadata;
    private final @Mandatory MainMenu mainMenu;
    private final @Mandatory SplitPanel splitPanel;
    private final @Mandatory ScenePanel scenePanel;
    private final @Mandatory PropertiesPanel propertiesPanel;

    public CrispWindow(@Mandatory Metadata metadata) {
        this.metadata = metadata;

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setTitle(TITLE);
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setLocationRelativeTo(null);

        mainMenu = new MainMenu(this);
        setJMenuBar(mainMenu);

        scenePanel = new ScenePanel();
        scenePanel.setFragmentSingleSelectListener(this::onFragmentSelected);
        scenePanel.setFragmentOpenListener(this::onFragmentOpened);

        propertiesPanel = new PropertiesPanel();
        propertiesPanel.setFragmentOpenListener(this::onFragmentOpened);
        propertiesPanel.setFragmentRowSelectListener(this::onFragmentRowSelected);

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

    private void onFragmentSelected(@Optional Fragment fragment) {
        propertiesPanel.setFragment(fragment);
    }


    private void onFragmentRowSelected(@Optional Fragment fragment, int i) {
        if (scenePanel.getScene() != null) {
            // TODO - implement
        }
        repaint();
    }

    private void onFragmentOpened(@Mandatory Fragment parent, int i) {
        if (scenePanel.getScene() != null) {
            openService.open(metadata, scenePanel.getScene(), parent, i);
            repaint();
        }
    }
}
