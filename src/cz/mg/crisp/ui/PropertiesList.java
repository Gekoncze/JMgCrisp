package cz.mg.crisp.ui;

import cz.mg.annotations.classes.Utility;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Optional;
import cz.mg.crisp.entity.metadata.ClassMetadata;
import cz.mg.crisp.entity.metadata.Metadata;
import cz.mg.crisp.services.DataReader;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import java.awt.*;

public @Utility class PropertiesList extends JList<Object> {
    private final @Mandatory Metadata metadata;
    private @Optional Object object;

    public PropertiesList(@Mandatory Metadata metadata) {
        this.metadata = metadata;
    }

    public @Optional Object getObject() {
        return object;
    }

    public void setObject(@Optional Object object) {
        this.object = object;
        rebuild();
    }

    private void rebuild() {
        setModel(new MetadataListModel(metadata, object));
        setCellRenderer(new MetadataCellRenderer(metadata));
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
                ClassMetadata classMetadata = metadata.get(object);
                return classMetadata.getFields().count();
            } else {
                return 0;
            }
        }

        @Override
        public Object getElementAt(int i) {
            if (object != null) {
                ClassMetadata classMetadata = metadata.get(object);
                return classMetadata.getFields().get(i).getValue(object);
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

        public MetadataCellRenderer(@Mandatory Metadata metadata) {
            this.metadata = metadata;
        }

        @Override
        public @Mandatory Component getListCellRendererComponent(
            @Mandatory JList<?> list,
            @Optional Object object,
            int i,
            boolean selected,
            boolean focused
        ) {
            if (object != null) {
                JLabel label = new JLabel();
                label.setText(dataReader.getRow(metadata, object, i));
                return label;
            } else {
                return new JLabel();
            }
        }
    }
}
