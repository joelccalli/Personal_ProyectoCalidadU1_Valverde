package com.charmeleon.controlador;

import com.charmeleon.modeloDAO.CargoDAO;
import com.charmeleon.vista.FrmCargos;
import com.charmeleon.vista.FrmModCargo;
import com.charmeleon.VistaRecursos.ButtonEditor;
import com.charmeleon.VistaRecursos.ButtonEditorCargo;
import com.charmeleon.VistaRecursos.ButtonRenderer;
import com.charmeleon.VistaRecursos.CustomButtonRenderer;
import com.charmeleon.modelo.Cargo;
import java.awt.Color;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.SwingConstants;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ControladorCargos implements ActionListener {
    CargoDAO cargosDAO = new CargoDAO();
    FrmCargos cargosView = new FrmCargos();
    FrmModCargo cargoModView = new FrmModCargo();
    Cargo cargo = new Cargo();
    DefaultTableModel modelo = new DefaultTableModel();
      
    public ControladorCargos(FrmCargos view) {
         this.cargosView = view;
    this.cargosView.btnAgregarCargo.addActionListener(this);
    ListarCargos(cargosView.cargosTable);
    }

    public ControladorCargos(FrmModCargo view) {
        this.cargoModView = view;
        this.cargoModView.btnRegresarCargoMod.addActionListener(this);
        this.cargoModView.btnActualizarCargo.addActionListener(this);
        ListarCargos(cargosView.cargosTable);
    }
    

    public void actionPerformed(ActionEvent e) {
        boolean detallesClicked = false;

        if (e.getSource() == cargosView.btnAgregarCargo) {
            AgregarCargo();
            ListarCargos(cargosView.cargosTable);
            limpiarCampos(cargosView);
        } else if (e.getSource() == cargoModView.btnRegresarCargoMod) {
            ActualizarTabla();
            CerrarCargoMod();

        } else if (e.getSource() == cargoModView.btnActualizarCargo) {
            ActualizarCargo();
        } else {
            JTable table = (JTable) e.getSource();
            int row = table.getSelectedRow();
            int col = table.getSelectedColumn();
            if (row != -1 && col != -1) {
                int modelRow = table.convertRowIndexToModel(row);
                int modelCol = table.convertColumnIndexToModel(col);
                if (modelCol == 3) { // Editar
                    int id = Integer.parseInt(table.getValueAt(modelRow, 0).toString());
                    abrirFormularioEditar(id);
                    cargosView.dispose();
                } else if (modelCol == 4) { // Eliminar
                    int id = Integer.parseInt(table.getValueAt(modelRow, 0).toString());
                    EliminarCargo(id);
                } else if (modelCol == 5) { // Detalles
                    int id = Integer.parseInt(table.getValueAt(modelRow, 0).toString());
                    detallesClicked = true;
                    mostrarDetalles(id);
                }
            }
        }

        if (detallesClicked) {
            limpiarTabla();
        }
    }

    public void ListarCargos(JTable tabla) {
        centrarCeldas(tabla);
        DefaultTableModel modelo = new DefaultTableModel(new Object[]{"ID", "Nombre", "Descripción", "Editar", "Eliminar", "Detalles"}, 0);
        List<Cargo> lista = cargosDAO.listarCargo();
        for (Cargo cargo : lista) {
            Object[] fila = new Object[6];
            fila[0] = cargo.getId();
            fila[1] = cargo.getNombre();
            fila[2] = cargo.getDescripcion();
            fila[3] = "Editar";
            fila[4] = "Eliminar";
            fila[5] = "Detalles";
            modelo.addRow(fila);
        }
        tabla.setModel(modelo);
        tabla.setRowHeight(25);
        tabla.setDefaultRenderer(Object.class, new ButtonRenderer());

        // Establecer un editor personalizado solo para las columnas de botones
        ButtonEditorCargo buttonEditor = new ButtonEditorCargo(this, cargosView);
        TableColumnModel columnModel = tabla.getColumnModel();

        // Obtener las columnas correspondientes a "Editar", "Eliminar" y "Detalles"
        TableColumn editarColumn = columnModel.getColumn(3);
        TableColumn eliminarColumn = columnModel.getColumn(4);
        TableColumn detallesColumn = columnModel.getColumn(5);

        // Establecer el editor de botones solo para esas columnas
        editarColumn.setCellEditor(buttonEditor);
        eliminarColumn.setCellEditor(buttonEditor);
        detallesColumn.setCellEditor(buttonEditor);
        
        editarColumn.setCellRenderer(new CustomButtonRenderer(Color.GREEN));
        eliminarColumn.setCellRenderer(new CustomButtonRenderer(Color.RED));
        detallesColumn.setCellRenderer(new CustomButtonRenderer(Color.YELLOW));
    }

    public void centrarCeldas(JTable tabla) {
        DefaultTableCellRenderer modelocentrar = new DefaultTableCellRenderer();
        modelocentrar.setHorizontalAlignment(SwingConstants.CENTER);
        TableColumnModel columnModel = tabla.getColumnModel();

        for (int i = 0; i < columnModel.getColumnCount(); i++) {
            TableColumn column = columnModel.getColumn(i);
            column.setCellRenderer(modelocentrar);
        }
    }

    public void AgregarCargo() {
        int id = Integer.parseInt(cargosView.txtIdCargo.getText());
        String nombre = cargosView.txtNomCar.getText();
        String descripcion = cargosView.txtDescripcioncarg.getText();
        if (nombre.equals("") || descripcion.equals("")) {
            JOptionPane.showMessageDialog(null, "Debe llenar todos los campos", "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
        } else {
            cargo.setId(id);
            cargo.setNombre(nombre);
            cargo.setDescripcion(descripcion);

            int r = cargosDAO.agregarCargo(cargo);
            if (r == 1) {
                JOptionPane.showMessageDialog(null, "Cargo agregado correctamente");
            } else {
                JOptionPane.showMessageDialog(null, "Error al agregar cargo");
            }
        }
    }

    public void limpiarCampos(FrmCargos cargosView) {
        cargosView.txtNomCar.setText("");
        cargosView.txtDescripcioncarg.setText("");
    }

    public void CerrarCargoMod() {
        cargoModView.dispose();
    }

    public void ActualizarCargo() {
        if (cargoModView.txtIdCargoMod.getText().equals("")) {
            JOptionPane.showMessageDialog(cargoModView, "No se identifica el ID. Debe seleccionar la opción Editar.");
        } else {
            int id = Integer.parseInt(cargoModView.txtIdCargoMod.getText());
            String nombre = cargoModView.txtNombreCargoMod.getText();
            String descripcion = cargoModView.txtDescCarMod.getText();

            if (nombre.equals("") || descripcion.equals("")) {
                JOptionPane.showMessageDialog(cargoModView, "Debe llenar todos los campos", "Advertencia", JOptionPane.WARNING_MESSAGE);
            } else {
                cargo.setId(id);
                cargo.setNombre(nombre);
                cargo.setDescripcion(descripcion);

                int r = cargosDAO.actualizarCargo(cargo);
                if (r == 1) {
                    JOptionPane.showMessageDialog(cargoModView, "Cargo actualizado correctamente");
                    ListarCargos(cargosView.cargosTable); // Actualizar la tabla
                    cargosView.cargosTable.repaint();
                } else {
                    JOptionPane.showMessageDialog(cargoModView, "Error al actualizar el cargo");
                }
            }
        }
    }

    public void ActualizarTabla() {
        ListarCargos(cargosView.cargosTable);
    }

    public void abrirFormularioEditar(int id) {
        cargoModView.setVisible(true);
        cargoModView.setLocationRelativeTo(null);
        cargoModView.setResizable(false);

        Cargo c = cargosDAO.obtenerCargoPorId(id);
        cargoModView.txtIdCargoMod.setText(String.valueOf(c.getId()));
        cargoModView.txtNombreCargoMod.setText(c.getNombre());
        cargoModView.txtDescCarMod.setText(c.getDescripcion());
        cargosView.dispose();
    }

    public void EliminarCargo(int id) {
        int opcion = JOptionPane.showConfirmDialog(null, "¿Desea eliminar el cargo?", "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION);
        if (opcion == JOptionPane.YES_OPTION) {
            int r = cargosDAO.eliminarCargo(id);
            if (r == 1) {
                JOptionPane.showMessageDialog(null, "Cargo eliminado correctamente");
                ListarCargos(cargosView.cargosTable);
            } else {
                JOptionPane.showMessageDialog(null, "Error al eliminar cargo");
            }
        }
    }

    public void limpiarTabla() {
        DefaultTableModel tb = (DefaultTableModel) cargosView.cargosTable.getModel();
        int a = cargosView.cargosTable.getRowCount() - 1;
        for (int i = a; i >= 0; i--) {
            tb.removeRow(tb.getRowCount() - 1);
        }
    }

    public void mostrarDetalles(int id) {
        Cargo c = cargosDAO.obtenerCargoPorId(id);
        JOptionPane.showMessageDialog(null,
                "ID: " + c.getId() + "\n" +
                        "Nombre: " + c.getNombre() + "\n" +
                        "Descripción: " + c.getDescripcion(), "Detalles del cargo", JOptionPane.INFORMATION_MESSAGE);
    }
}
