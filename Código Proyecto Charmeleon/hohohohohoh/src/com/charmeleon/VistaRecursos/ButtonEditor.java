package com.charmeleon.VistaRecursos;

import com.charmeleon.controlador.ControladorProductos;
import com.charmeleon.vista.FrmProductos;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

public class ButtonEditor extends AbstractCellEditor implements TableCellEditor {
    protected JButton button;
    private String label;
    private boolean isPushed;
    private ControladorProductos controladorProductos;
    private FrmProductos prod;

    public ButtonEditor(ControladorProductos controladorProductos, FrmProductos prod) {
        this.controladorProductos = controladorProductos;
        this.prod = prod;
        button = new JButton();
        button.setOpaque(true);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fireEditingStopped();
            }
        });
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
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
        return button;
    }

    public Object getCellEditorValue() {
        if (isPushed) {
            int selectedRow = prod.productosTable.getSelectedRow();
            if (selectedRow != -1) {
                int productId = (int) prod.productosTable.getValueAt(selectedRow, 0);
                int column = prod.productosTable.getSelectedColumn();

                switch (column) {
                    case 6: // Botón Editar
                        controladorProductos.abrirFormularioEditar(productId);
                        break;
                    case 7: // Botón Eliminar
                        controladorProductos.EliminarProductos(productId);
                        break;
                    case 8: // Botón Detalles
                        controladorProductos.mostrarDetalles(productId);
                        break;
                    default:
                        break;
                }
            }
        }
        isPushed = false;
        return new String(label);
    }

    public boolean stopCellEditing() {
        isPushed = false;
        return super.stopCellEditing();
    }

    protected void fireEditingStopped() {
        super.fireEditingStopped();
    }
}
