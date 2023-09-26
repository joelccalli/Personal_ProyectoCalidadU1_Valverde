package com.charmeleon.VistaRecursos;

import com.charmeleon.controlador.ControladorCargos;
import com.charmeleon.vista.FrmCargos;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JTable;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonEditorCargo extends DefaultCellEditor {
    protected JButton button;
    private String label;
    private boolean isPushed;
    private ControladorCargos controlador;
    private FrmCargos cargosView;

    public ButtonEditorCargo(ControladorCargos controlador, FrmCargos cargosView) {
        super(new JCheckBox());
        this.controlador = controlador;
        this.cargosView = cargosView;
        button = new JButton();
        button.setOpaque(true);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fireEditingStopped();
            }
        });
    }

public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
    if (column == 3 || column == 4 || column == 5) {
        if (isSelected) {
            button.setForeground(table.getSelectionForeground());
            button.setBackground(table.getSelectionBackground());
        } else {
            button.setForeground(table.getForeground());
            button.setBackground(table.getBackground());
        }

        label = (value == null) ? "" : value.toString();
        button.setText(label);
        isPushed = true;

        if (column == 4) { // Columna "Eliminar"
            button.setBackground(Color.RED);
        } else if (column == 5) { // Columna "Detalles"
            button.setBackground(Color.YELLOW);
        } else if (column == 6) { // Columna "Editar"
            button.setBackground(Color.GREEN);
        }
    } else {
        // Si no es una columna de botones, no se muestra como botón
        button.setForeground(table.getForeground());
        button.setBackground(table.getBackground());
        label = (value == null) ? "" : value.toString();
        button.setText(label);
        isPushed = false;
    }

    // Deshabilitar la edición para las columnas 0, 1 y 2
    if (column == 0 || column == 1 || column == 2) {
        button.setEnabled(false);
    } else {
        button.setEnabled(true);
    }

    return button;
}

    public Object getCellEditorValue() {
        if (isPushed) {
            // Acción al hacer clic en el botón
            int row = cargosView.cargosTable.getSelectedRow();
            int col = cargosView.cargosTable.getSelectedColumn();
            if (col == 3) { // Editar
                int id = Integer.parseInt(cargosView.cargosTable.getValueAt(row, 0).toString());
                controlador.abrirFormularioEditar(id);
                cargosView.dispose();
            } else if (col == 4) { // Eliminar
                int id = Integer.parseInt(cargosView.cargosTable.getValueAt(row, 0).toString());
                controlador.EliminarCargo(id);
            } else if (col == 5) { // Detalles
                int id = Integer.parseInt(cargosView.cargosTable.getValueAt(row, 0).toString());
                controlador.mostrarDetalles(id);
            }
        }
        isPushed = false;
        return label;
    }

    public boolean stopCellEditing() {
        isPushed = false;
        return super.stopCellEditing();
    }

    protected void fireEditingStopped() {
        super.fireEditingStopped();
    }
}
