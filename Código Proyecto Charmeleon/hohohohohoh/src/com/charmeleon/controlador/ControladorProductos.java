package com.charmeleon.controlador;

import com.charmeleon.modelo.Productos;
import com.charmeleon.modeloDAO.ProductosDAO;
import com.charmeleon.vista.FrmProductos;
import com.charmeleon.vista.FrmProdMod;
import com.charmeleon.VistaRecursos.ButtonEditor;
import com.charmeleon.VistaRecursos.ButtonRenderer;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.SwingConstants;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ControladorProductos implements ActionListener {
    ProductosDAO proDAO = new ProductosDAO();
    FrmProductos prod = new FrmProductos();
    FrmProdMod prodmod = new FrmProdMod();
    Productos pr = new Productos();
    DefaultTableModel modelo = new DefaultTableModel();

    public ControladorProductos(FrmProductos Pr) {
        this.prod = Pr;
        this.prod.btnAgregarProd.addActionListener(this);
        ListarProducto(prod.productosTable);
    }

    public ControladorProductos(FrmProdMod Pm) {
        this.prodmod = Pm;
        this.prodmod.btnRegresarProdMod.addActionListener(this);
        this.prodmod.btnActualizarPro.addActionListener(this);
        ListarProducto(prod.productosTable);
    }

    public void actionPerformed(ActionEvent e) {
        boolean detallesClicked = false;

        if (e.getSource() == prod.btnAgregarProd) {
            AgregarProductos();
            ListarProducto(prod.productosTable);
            limpiarCampos(prod);
        } else if (e.getSource() == prodmod.btnRegresarProdMod) {
            ActualizarTabla();
            CerrarProdMod();
        } else if (e.getSource() == prodmod.btnActualizarPro) {
            Actualizarproducto();
        } else {
            JTable table = prod.productosTable;
            int row = table.getSelectedRow();
            int col = table.getSelectedColumn();
            if (row != -1 && col != -1) {
                int modelRow = table.convertRowIndexToModel(row);
                int modelCol = table.convertColumnIndexToModel(col);
                if (modelCol == 6) { // Editar
                    int id = Integer.parseInt(table.getValueAt(modelRow, 0).toString());
                    abrirFormularioEditar(id);
                    prod.dispose();
                } else if (modelCol == 7) { // Eliminar
                    int id = Integer.parseInt(table.getValueAt(modelRow, 0).toString());
                    EliminarProductos(id);
                } else if (modelCol == 8) { // Detalles
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

    public void ListarProducto(JTable tabla) {
        centrarCeldas(tabla);
        modelo = new DefaultTableModel(
            new Object[]{"ID", "Nombre", "Descripción", "Precio", "Cantidad", "Categoría", "Editar", "Eliminar", "Detalles"},
            0
        );
        List<Productos> lista = proDAO.listarprod();
        for (Productos producto : lista) {
            Object[] fila = new Object[9];
            fila[0] = producto.getId();
            fila[1] = producto.getNombre();
            fila[2] = producto.getDescripcion();
            fila[3] = producto.getPrecio();
            fila[4] = producto.getCantidad();
            fila[5] = producto.getCategoria();
            fila[6] = "Editar";
            fila[7] = "Eliminar";
            fila[8] = "Detalles";
            modelo.addRow(fila);
        }
        tabla.setModel(modelo);
        tabla.setRowHeight(25);
        tabla.setDefaultRenderer(Object.class, new ButtonRenderer());
        ButtonEditor buttonEditor = new ButtonEditor(this, prod);
        tabla.setDefaultEditor(Object.class, buttonEditor);
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

    public void AgregarProductos() {
        int id = Integer.parseInt(prod.txtIdPro.getText());
        String nombre = prod.txtNomPro.getText();
        String descripcion = prod.txtDescripcionPro.getText();
        double precio = Double.parseDouble(prod.txtPreciopro.getText());
        int cantidad = Integer.parseInt(prod.txtCantPro.getText());
        JComboBox<String> comboBoxCategoria = prod.cmbCategoriaPro;

        if (nombre.equals("") || descripcion.equals("") || precio == 0 || cantidad == 0) {
            JOptionPane.showMessageDialog(null, "Debe llenar todos los campos", "Advertencia", JOptionPane.WARNING_MESSAGE);
        } else {
            pr.setId(id);
            pr.setNombre(nombre);
            pr.setDescripcion(descripcion);
            pr.setPrecio(precio);
            pr.setCantidad(cantidad);
            pr.setCategoria(comboBoxCategoria.getSelectedItem().toString());
            int r = proDAO.AgregarProd(pr);
            if (r == 1) {
                JOptionPane.showMessageDialog(null, "Producto agregado correctamente");
            } else {
                JOptionPane.showMessageDialog(null, "Error al agregar producto");
            }
        }
    }

    public void limpiarCampos(FrmProductos prod) {
        prod.txtNomPro.setText("");
        prod.txtDescripcionPro.setText("");
        prod.txtPreciopro.setText("");
        prod.txtCantPro.setText("");
    }

    public void CerrarProdMod() {
        prodmod.dispose();
    }

    public void Actualizarproducto() {
        if (prodmod.txtIdProdMod.getText().equals("")) {
            JOptionPane.showMessageDialog(prodmod, "No se identifica el ID. Debe seleccionar la opción Editar.");
        } else {
            int id = Integer.parseInt(prodmod.txtIdProdMod.getText());
            String nombre = prodmod.txtNomProdMod.getText();
            String descripcion = prodmod.txtDescProdMod.getText();
            double precio = Double.parseDouble(prodmod.txtPreProdMod.getText());
            int cantidad = Integer.parseInt(prodmod.txtCanProdMod.getText());
            JComboBox<String> comboBoxCategoria = prodmod.cmbCatProdMod;

            if (nombre.equals("") || descripcion.equals("") || precio == 0 || cantidad == 0) {
                JOptionPane.showMessageDialog(prodmod, "Debe llenar todos los campos", "Advertencia", JOptionPane.WARNING_MESSAGE);
            } else {
                pr.setId(id);
                pr.setNombre(nombre);
                pr.setDescripcion(descripcion);
                pr.setPrecio(precio);
                pr.setCantidad(cantidad);
                pr.setCategoria(comboBoxCategoria.getSelectedItem().toString());

                int r = proDAO.ActualizarProd(pr);
                if (r == 1) {
                    JOptionPane.showMessageDialog(prodmod, "Producto actualizado correctamente");
                    ListarProducto(prod.productosTable); // Actualizar la tabla
                    prod.productosTable.repaint();
                } else {
                    JOptionPane.showMessageDialog(prodmod, "Error al actualizar el producto");
                }
            }
        }
    }

    public void ActualizarTabla() {
        ListarProducto(prod.productosTable);
    }

    public void abrirFormularioEditar(int id) {
        prodmod.setVisible(true);
        prodmod.setLocationRelativeTo(null);
        prodmod.setResizable(false);
        prodmod.cmbCatProdMod.removeAllItems();
        prodmod.cmbCatProdMod.addItem("1");
        prodmod.cmbCatProdMod.addItem("2");
        prodmod.cmbCatProdMod.addItem("3");
        prodmod.cmbCatProdMod.addItem("4");
        prodmod.cmbCatProdMod.addItem("5");
        prodmod.cmbCatProdMod.addItem("Ot6ros");

        Productos p = proDAO.obtenerProductoPorId(id);
        prodmod.txtIdProdMod.setText(String.valueOf(p.getId()));
        prodmod.txtNomProdMod.setText(p.getNombre());
        prodmod.txtDescProdMod.setText(p.getDescripcion());
        prodmod.txtPreProdMod.setText(String.valueOf(p.getPrecio()));
        prodmod.txtCanProdMod.setText(String.valueOf(p.getCantidad()));
        prodmod.cmbCatProdMod.setSelectedItem(p.getCategoria());
        prod.dispose();
    }

    public void EliminarProductos(int id) {
        int opcion = JOptionPane.showConfirmDialog(null, "¿Desea eliminar el producto?", "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION);
        if (opcion == JOptionPane.YES_OPTION) {
            int r = proDAO.EliminarProd(id);
            if (r == 1) {
                JOptionPane.showMessageDialog(null, "Producto eliminado correctamente");
                ListarProducto(prod.productosTable);
            } else {
                JOptionPane.showMessageDialog(null, "Error al eliminar producto");
            }
        }
    }

    public void limpiarTabla() {
        DefaultTableModel tb = (DefaultTableModel) prod.productosTable.getModel();
        int a = prod.productosTable.getRowCount() - 1;
        for (int i = a; i >= 0; i--) {
            tb.removeRow(tb.getRowCount() - 1);
        }
    }

    public void mostrarDetalles(int id) {
        Productos p = proDAO.obtenerProductoPorId(id);
        JOptionPane.showMessageDialog(null,
                "ID: " + p.getId() + "\n" +
                        "Nombre: " + p.getNombre() + "\n" +
                        "Descripción: " + p.getDescripcion() + "\n" +
                        "Precio: " + p.getPrecio() + "\n" +
                        "Cantidad: " + p.getCantidad() + "\n" +
                        "Categoría: " + p.getCategoria(), "Detalles del producto", JOptionPane.INFORMATION_MESSAGE);
    }
}
