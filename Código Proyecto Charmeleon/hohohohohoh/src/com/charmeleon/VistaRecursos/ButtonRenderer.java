package com.charmeleon.VistaRecursos;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class ButtonRenderer extends JButton implements TableCellRenderer {

    public ButtonRenderer() {
        setOpaque(true);
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        setText((value == null) ? "" : value.toString());

        if (column == 6) { // Editar
            setBackground(Color.GREEN);
        } else if (column == 7) { // Eliminar
            setBackground(Color.RED);
        } else if (column == 8) { // Detalles
            setBackground(Color.YELLOW);
        } else {
            setBackground(table.getBackground());
        }

        return this;
    }
}
