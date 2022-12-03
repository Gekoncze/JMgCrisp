package cz.mg.crisp.ui;

import cz.mg.annotations.classes.Utility;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Optional;
import cz.mg.crisp.entity.model.Fragment;
import cz.mg.crisp.entity.model.Row;
import cz.mg.crisp.event.UserMouseClickedListener;
import cz.mg.crisp.listeners.FragmentOpenListener;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import java.awt.*;
import java.awt.event.MouseEvent;

public @Utility class PropertiesList extends JList<Row> {
    private static final Color SELECTION_COLOR = UIManager.getDefaults().getColor("List.selectionBackground");

    private @Optional Fragment fragment;
    private @Optional FragmentOpenListener fragmentOpenListener;

    public PropertiesList() {
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        addMouseListener(new UserMouseClickedListener(this::onMouseClicked));
    }

    public @Optional Fragment getFragment() {
        return fragment;
    }

    public void setFragment(@Optional Fragment fragment) {
        this.fragment = fragment;
        rebuild();
    }

    public @Optional FragmentOpenListener getFragmentOpenListener() {
        return fragmentOpenListener;
    }

    public void setFragmentOpenListener(@Optional FragmentOpenListener fragmentOpenListener) {
        this.fragmentOpenListener = fragmentOpenListener;
    }

    private void rebuild() {
        setModel(new MetadataListModel(fragment));
        setCellRenderer(new MetadataCellRenderer());
    }

    private void onMouseClicked(@Mandatory MouseEvent event) {
        if (event.getClickCount() == 2) {
            int index = locationToIndex(event.getPoint());
            if (index >= 0 && index < getModel().getSize()) {
                if (fragmentOpenListener != null) {
                    if (fragment != null) {
                        fragmentOpenListener.onFragmentOpened(fragment, index);
                    }
                }
            }
        }
    }

    private static class MetadataListModel implements ListModel<Row> {
        private final @Optional Fragment fragment;

        public MetadataListModel(@Optional Fragment fragment) {
            this.fragment = fragment;
        }

        @Override
        public int getSize() {
            if (fragment != null) {
                return fragment.getRows().count();
            } else {
                return 0;
            }
        }

        @Override
        public Row getElementAt(int i) {
            if (fragment != null) {
                return fragment.getRows().get(i);
            } else {
                return null;
            }
        }

        @Override
        public void addListDataListener(ListDataListener listDataListener) {
        }

        @Override
        public void removeListDataListener(ListDataListener listDataListener) {
        }
    }

    private static class MetadataCellRenderer implements ListCellRenderer<Row> {
        public MetadataCellRenderer() {
        }

        @Override
        public @Mandatory Component getListCellRendererComponent(
            @Mandatory JList<? extends Row> list,
            @Optional Row row,
            int i,
            boolean selected,
            boolean focused
        ) {
            if (row != null) {
                JLabel label = new JLabel();
                label.setText(row.getName() + ": " + row.getValue());
                label.setOpaque(selected);
                label.setBackground(selected ? SELECTION_COLOR : Color.WHITE);
                return label;
            } else {
                return new JLabel();
            }
        }
    }
}
