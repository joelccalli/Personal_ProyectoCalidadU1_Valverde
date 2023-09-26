package com.charmeleon.VistaRecursos;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.Color;
import java.awt.Component;

public class CustomButtonRenderer extends DefaultTableCellRenderer {
    private JButton button;

    public CustomButtonRenderer(Color backgroundColor) {
        button = new JButton();
        button.setOpaque(true);
        button.setBackground(backgroundColor);
    }

   public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
    if (value instanceof JButton) {
        JButton button = (JButton) value;
        return button;
    } else {
        setText((value == null) ? "" : value.toString());

        if (column == 5) { // Columna "Anular"
            setBackground(Color.RED);
        } else if (column == 6) { // Columna "Detalles"
            setBackground(Color.YELLOW);
        } else {
            setBackground(table.getBackground());
        }

        return this;
    }
}
}
