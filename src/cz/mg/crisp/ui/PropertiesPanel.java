package cz.mg.crisp.ui;

import cz.mg.annotations.classes.Utility;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Optional;
import cz.mg.crisp.entity.model.Fragment;
import cz.mg.crisp.listeners.FragmentOpenListener;
import cz.mg.crisp.listeners.FragmentRowSelectListener;

import javax.swing.*;

public @Utility class PropertiesPanel extends Panel {
    private static final int PADDING = 4;

    private final @Mandatory JLabel label;
    private final @Mandatory PropertiesList propertiesList;

    public PropertiesPanel() {
        super(PADDING, PADDING);

        label = new JLabel();
        addVertical(label, 1, 0);

        propertiesList = new PropertiesList();
        addVertical(propertiesList, 1, 1);

        rebuild();
    }

    public void setFragment(@Optional Fragment fragment) {
        propertiesList.setFragment(fragment);
        label.setText(fragment == null ? "" : fragment.getHeader());
    }

    public @Optional Fragment getFragment() {
        return propertiesList.getFragment();
    }

    public @Optional FragmentOpenListener getFragmentOpenListener() {
        return propertiesList.getFragmentOpenListener();
    }

    public void setFragmentOpenListener(@Optional FragmentOpenListener fragmentOpenListener) {
        propertiesList.setFragmentOpenListener(fragmentOpenListener);
    }

    public @Optional FragmentRowSelectListener getFragmentRowSelectListener() {
        return propertiesList.getFragmentRowSelectListener();
    }

    public void setFragmentRowSelectListener(@Optional FragmentRowSelectListener fragmentRowSelectListener) {
        propertiesList.setFragmentRowSelectListener(fragmentRowSelectListener);
    }
}
