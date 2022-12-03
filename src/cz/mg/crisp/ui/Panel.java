package cz.mg.crisp.ui;

import cz.mg.annotations.classes.Utility;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.collections.list.List;

import javax.swing.*;
import java.awt.*;

import static cz.mg.crisp.ui.Panel.Alignment.MIDDLE;
import static cz.mg.crisp.ui.Panel.Alignment.TOP_LEFT;
import static cz.mg.crisp.ui.Panel.Fill.BOTH;


public @Utility class Panel extends JPanel {
    private final int border;
    private final int padding;
    private final @Mandatory HorizontalAlignment horizontalAlignment;
    private final @Mandatory VerticalAlignment verticalAlignment;
    private final @Mandatory List<ComponentSettings> components = new List<>();

    public Panel(int border, int padding) {
        this(border, padding, TOP_LEFT);
    }

    public Panel(int border, int padding, @Mandatory Alignment alignment) {
        this.border = border;
        this.padding = padding;
        this.horizontalAlignment = alignment.getHorizontal();
        this.verticalAlignment = alignment.getVertical();
        setOpaque(false);
        setBackground(null);
        rebuild();
    }

    public void add(
        @Mandatory Component component,
        int x, int y, int wx, int wy,
        @Mandatory Alignment alignment,
        @Mandatory Fill fill
    ){
        add(component, x, y, wx, wy, alignment, fill, 1, 1);
    }

    public void add(
        @Mandatory Component component,
        int x, int y, int wx, int wy,
        @Mandatory Alignment alignment,
        @Mandatory Fill fill,
        int spanX, int spanY
    ){
        components.addLast(new ComponentSettings(component, x, y, wx, wy, alignment, fill, spanX, spanY));
    }

    public void addHorizontal(@Mandatory Component component, int wx, int wy) {
        addHorizontal(component, wx, wy, MIDDLE, BOTH);
    }

    public void addHorizontal(
        @Mandatory Component component,
        int wx, int wy,
        @Mandatory Alignment alignment,
        @Mandatory Fill fill
    ) {
        add(component, components.count(), 0, wx, wy, alignment, fill);
    }

    public void addVertical(
        @Mandatory Component component,
        int wx, int wy,
        @Mandatory Alignment alignment,
        @Mandatory Fill fill
    ) {
        add(component, 0, components.count(), wx, wy, alignment, fill);
    }

    public void clear() {
        removeAll();
        components.clear();
    }

    public void rebuild() {
        removeAll();
        setLayout(new GridBagLayout());

        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;
        int maxWX = 0;
        int maxWY = 0;

        for (ComponentSettings component : components) {
            minX = Math.min(minX, component.x);
            minY = Math.min(minY, component.y);
            maxX = Math.max(maxX, component.x);
            maxY = Math.max(maxY, component.y);
            maxWX = Math.max(maxWX, component.wx);
            maxWY = Math.max(maxWY, component.wy);
        }

        int dx = 1 + minX >= 0 ? 0 : -minX;
        int dy = 1 + minY >= 0 ? 0 : -minY;

        for (ComponentSettings component : components) {
            int pLeft = component.x == minX ? border : 0;
            int pRight = component.x == maxX ? border : padding;
            int pTop = component.y == minY ? border : 0;
            int pBottom = component.y == maxY ? border : padding;

            add(component.component, constraints(
                component.x + dx, component.y + dy, component.wx, component.wy,
                pTop, pLeft, pBottom, pRight,
                component.alignment, component.fill,
                component.spanX, component.spanY
            ));
        }

        if (maxWX == 0) {
            if (horizontalAlignment == HorizontalAlignment.LEFT) {
                add(createDummy(), constraints(-1 + dx, dy, 1, 0, 0, 0, 0, 0, MIDDLE, BOTH, 1, 1));
            }

            if (horizontalAlignment == HorizontalAlignment.RIGHT) {
                add(createDummy(), constraints(maxX + 1 + dx, dy, 1, 0, 0, 0, 0, 0, MIDDLE, BOTH, 1, 1));
            }
        }

        if (maxWY == 0) {
            if (verticalAlignment == VerticalAlignment.TOP) {
                add(createDummy(), constraints(dx, -1 + dy, 0, 1, 0, 0, 0, 0, MIDDLE, BOTH, 1, 1));
            }

            if (verticalAlignment == VerticalAlignment.BOTTOM) {
                add(createDummy(), constraints(dx, maxY + 1 + dy, 0, 1, 0, 0, 0, 0, MIDDLE, BOTH, 1, 1));
            }
        }

        repaint();
        revalidate();
    }

    private @Mandatory JLabel createDummy(){
        JLabel dummy = new JLabel();
        dummy.setMinimumSize(new Dimension(0, 0));
        dummy.setPreferredSize(new Dimension(0, 0));
        dummy.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        dummy.setOpaque(false);
        dummy.setBackground(null);
        return dummy;
    }

    private @Mandatory GridBagConstraints constraints(
        int x, int y, int wx, int wy,
        int pTop, int pLeft, int pBottom, int pRight,
        @Mandatory Alignment alignment,
        @Mandatory Fill fill,
        int spanX, int spanY
    ){
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = x;
        constraints.gridy = y;
        constraints.weightx = wx;
        constraints.weighty = wy;
        constraints.fill = fill.getInternalCode();
        constraints.insets = new Insets(pTop, pLeft, pBottom, pRight);
        constraints.anchor = alignment.getInternalAnchor();
        constraints.gridwidth = spanX;
        constraints.gridheight = spanY;
        return constraints;
    }

    private static @Utility class ComponentSettings {
        public final @Mandatory Component component;
        public final int x;
        public final int y;
        public final int wx;
        public final int wy;
        public final @Mandatory Alignment alignment;
        public final @Mandatory Fill fill;
        public final int spanX;
        public final int spanY;

        public ComponentSettings(
            @Mandatory Component component,
            int x, int y, int wx, int wy,
            @Mandatory Alignment alignment,
            @Mandatory Fill fill,
            int spanX, int spanY
        ) {
            this.component = component;
            this.x = x;
            this.y = y;
            this.wx = wx;
            this.wy = wy;
            this.alignment = alignment;
            this.fill = fill;
            this.spanX = spanX;
            this.spanY = spanY;
        }
    }

    public enum Fill {
        NONE,
        HORIZONTAL,
        VERTICAL,
        BOTH;

        public int getInternalCode() {
            switch (this) {
                case NONE:
                    return GridBagConstraints.NONE;
                case HORIZONTAL:
                    return GridBagConstraints.HORIZONTAL;
                case VERTICAL:
                    return GridBagConstraints.VERTICAL;
                case BOTH:
                    return GridBagConstraints.BOTH;
            }
            throw new IllegalStateException();
        }
    }

    public enum Alignment {
        TOP_LEFT, TOP, TOP_RIGHT,
        LEFT, MIDDLE, RIGHT,
        BOTTOM_LEFT, BOTTOM, BOTTOM_RIGHT;

        public @Mandatory HorizontalAlignment getHorizontal() {
            switch (this) {
                case TOP_LEFT:
                    return HorizontalAlignment.LEFT;
                case LEFT:
                    return HorizontalAlignment.LEFT;
                case BOTTOM_LEFT:
                    return HorizontalAlignment.LEFT;

                case TOP:
                    return HorizontalAlignment.MIDDLE;
                case MIDDLE:
                    return HorizontalAlignment.MIDDLE;
                case BOTTOM:
                    return HorizontalAlignment.MIDDLE;

                case TOP_RIGHT:
                    return HorizontalAlignment.RIGHT;
                case RIGHT:
                    return HorizontalAlignment.RIGHT;
                case BOTTOM_RIGHT:
                    return HorizontalAlignment.RIGHT;
            }
            throw new IllegalStateException();
        }

        public @Mandatory VerticalAlignment getVertical() {
            switch (this) {
                case TOP_LEFT:
                    return VerticalAlignment.TOP;
                case TOP:
                    return VerticalAlignment.TOP;
                case TOP_RIGHT:
                    return VerticalAlignment.TOP;

                case LEFT:
                    return VerticalAlignment.MIDDLE;
                case MIDDLE:
                    return VerticalAlignment.MIDDLE;
                case RIGHT:
                    return VerticalAlignment.MIDDLE;

                case BOTTOM_LEFT:
                    return VerticalAlignment.BOTTOM;
                case BOTTOM:
                    return VerticalAlignment.BOTTOM;
                case BOTTOM_RIGHT:
                    return VerticalAlignment.BOTTOM;
            }
            throw new IllegalStateException();
        }

        public int getInternalAnchor() {
            switch (this) {
                case TOP_LEFT:
                    return GridBagConstraints.FIRST_LINE_START;
                case TOP:
                    return GridBagConstraints.PAGE_START;
                case TOP_RIGHT:
                    return GridBagConstraints.FIRST_LINE_END;

                case LEFT:
                    return GridBagConstraints.LINE_START;
                case MIDDLE:
                    return GridBagConstraints.CENTER;
                case RIGHT:
                    return GridBagConstraints.LINE_END;

                case BOTTOM_LEFT:
                    return GridBagConstraints.LAST_LINE_START;
                case BOTTOM:
                    return GridBagConstraints.PAGE_END;
                case BOTTOM_RIGHT:
                    return GridBagConstraints.LAST_LINE_END;
            }
            throw new IllegalStateException();
        }
    }

    public enum HorizontalAlignment {
        LEFT, MIDDLE, RIGHT
    }

    public enum VerticalAlignment {
        TOP, MIDDLE, BOTTOM
    }
}

