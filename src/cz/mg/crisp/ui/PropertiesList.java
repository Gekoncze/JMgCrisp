package cz.mg.crisp.ui;

import cz.mg.annotations.classes.Utility;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Optional;
import cz.mg.crisp.entity.Fragment;
import cz.mg.crisp.entity.metadata.Metadata;
import cz.mg.crisp.event.UserMouseClickedListener;
import cz.mg.crisp.listeners.FragmentOpenListener;
import cz.mg.crisp.services.DataReader;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import java.awt.*;
import java.awt.event.MouseEvent;

public @Utility class PropertiesList extends JList<Object> {
    private static final Color SELECTION_COLOR = UIManager.getDefaults().getColor("List.selectionBackground");

    private final @Mandatory Metadata metadata;
    private @Optional Fragment fragment;
    private @Optional FragmentOpenListener fragmentOpenListener;

    public PropertiesList(@Mandatory Metadata metadata) {
        this.metadata = metadata;
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
        Object object = fragment != null ? fragment.getObject() : null;
        setModel(new MetadataListModel(metadata, object));
        setCellRenderer(new MetadataCellRenderer(metadata, object));
    }

    private void onMouseClicked(@Mandatory MouseEvent event) {
        if (event.getClickCount() == 2) {
            int index = locationToIndex(event.getPoint());
            if (index >= 0 && index < getModel().getSize()) {
                Object field = getModel().getElementAt(index);
                if (fragmentOpenListener != null) {
                    if (fragment != null) {
                        fragmentOpenListener.onFragmentOpened(fragment, field);
                    }
                }
            }
        }
    }

    private static class MetadataListModel implements ListModel<Object> {
        private final @Mandatory Metadata metadata;
        private final @Optional Object object;

        public MetadataListModel(@Mandatory Metadata metadata, @Optional Object object) {
            this.metadata = metadata;
            this.object = object;
        }

        @Override
        public int getSize() {
            if (object != null) {
                return metadata.get(object).getFields().count();
            } else {
                return 0;
            }
        }

        @Override
        public Object getElementAt(int i) {
            if (object != null) {
                return metadata.get(object).getFields().get(i).getValue(object);
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

    private static class MetadataCellRenderer implements ListCellRenderer<Object> {
        private final @Mandatory DataReader dataReader = DataReader.getInstance();
        private final @Mandatory Metadata metadata;
        private final @Optional Object object;

        public MetadataCellRenderer(@Mandatory Metadata metadata, @Optional Object object) {
            this.metadata = metadata;
            this.object = object;
        }

        @Override
        public @Mandatory Component getListCellRendererComponent(
            @Mandatory JList<?> list,
            @Optional Object fieldValue,
            int i,
            boolean selected,
            boolean focused
        ) {
            if (object != null) {
                JLabel label = new JLabel();
                label.setText(dataReader.getRow(metadata, object, fieldValue, i));
                label.setOpaque(selected);
                label.setBackground(selected ? SELECTION_COLOR : Color.WHITE);
                return label;
            } else {
                return new JLabel();
            }
        }
    }
}
