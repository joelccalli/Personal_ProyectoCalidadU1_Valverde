package com.charmeleon.controlador;

import com.charmeleon.modelo.Persona;
import com.charmeleon.modeloDAO.PersonaDAO;
import com.charmeleon.modelo.Productos;
import com.charmeleon.modeloDAO.ProductosDAO;
import com.charmeleon.vista.Empleado;
import com.charmeleon.vista.FrmLogin;
import com.charmeleon.vista.FrmMenu;
import com.charmeleon.vista.FrmProductos;
import com.charmeleon.VistaRecursos.RenderTable;
import com.charmeleon.vista.FrmBoletas;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
public class Controlador implements ActionListener {

    PersonaDAO dao = new PersonaDAO();
 
    Persona p = new Persona();
    
    Empleado vista = new Empleado();
    FrmLogin login = new FrmLogin();
    FrmMenu menu = new FrmMenu();
    
    DefaultTableModel modelo = new DefaultTableModel();
    private String nombreUsuario;

    public Controlador(Empleado v) {
        this.vista = v;
        this.vista.btnListar.addActionListener(this);
        this.vista.btnAgregar.addActionListener(this);
        this.vista.btnEditar.addActionListener(this);
        this.vista.btnDelete.addActionListener(this);
        this.vista.btnActualizar.addActionListener(this);
        this.vista.btnNuevo.addActionListener(this);
   
    }
 public Controlador(FrmLogin l) {
        this.login = l;
        this.login.btnIngresar.addActionListener(this);
    }
public Controlador(FrmMenu M) {
        this.menu = M;
        this.menu.btnEmpleados.addActionListener(this);
        this.menu.setHoraActual();
        
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.btnListar) {
            limpiarTabla();
            listar(vista.tabla);
            nuevo();
           
        }
        if (e.getSource() == vista.btnAgregar) {
            add();
            listar(vista.tabla);
            nuevo();
        }
        if (e.getSource() == vista.btnEditar) {
            int fila = vista.tabla.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(vista, "Debee Seleccionar Una fila..!!");
            } else {
                int id = Integer.parseInt((String) vista.tabla.getValueAt(fila, 0).toString());
                String nom = (String) vista.tabla.getValueAt(fila, 1);
                String correo = (String) vista.tabla.getValueAt(fila, 2);
                String tel = (String) vista.tabla.getValueAt(fila, 3);
                String cla = (String) vista.tabla.getValueAt(fila, 4);
                vista.txtId.setText("" + id);
                vista.txtNom.setText(nom);
                vista.txtCorreo.setText(correo);
                vista.txtTel.setText(tel);
                vista.txtCla.setText(cla);
            }
        }
        if (e.getSource() == vista.btnActualizar) {
            Actualizar();
            listar(vista.tabla);
            nuevo();

        }
        if (e.getSource() == vista.btnDelete) {
            delete();
            listar(vista.tabla);
            nuevo();
        }
        if (e.getSource() == vista.btnNuevo) {
            nuevo();
        }
      if (e.getSource() == login.btnIngresar) {
              Login();
      }
     


    }

    void nuevo() {
        vista.txtId.setText("");
        vista.txtNom.setText("");
        vista.txtTel.setText("");
        vista.txtCorreo.setText("");
        vista.txtCla.setText("");
        vista.txtNom.requestFocus();
    }

    public void delete() {
        int fila = vista.tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(vista, "Debe Seleccionar una Fila...!!!");
        } else {
            int id = Integer.parseInt((String) vista.tabla.getValueAt(fila, 0).toString());
            dao.Delete(id);
            System.out.println("El Reusltaod es" + id);
            JOptionPane.showMessageDialog(vista, "Usuario Eliminado...!!!");
        }
        limpiarTabla();
    }

    public void add() {
        int id=Integer.parseInt((String)vista.txtId.getText());
        String nom = vista.txtNom.getText();
        String correo = vista.txtCorreo.getText();
        String tel = vista.txtTel.getText();
        String cla = vista.txtCla.getText();
        p.setId(id);
        p.setNom(nom);
        p.setCorreo(correo);
        p.setTelefono(tel);
        p.setClave(cla);
        int r = dao.agregar(p);
        if (r == 1) {
            JOptionPane.showMessageDialog(vista, "Usuario Agregado con Exito.");
        } else {
            JOptionPane.showMessageDialog(vista, "Error");
        }
        limpiarTabla();
    }

    public void Actualizar() {
        if (vista.txtId.getText().equals("")) {
            JOptionPane.showMessageDialog(vista, "No se Identifica el Id debe selecionar la opcion Editar");
        } else {
            int id = Integer.parseInt(vista.txtId.getText());
            String nom = vista.txtNom.getText();
            String correo = vista.txtCorreo.getText();
            String tel = vista.txtTel.getText();
            String cla = vista.txtCla.getText();
            p.setId(id);
            p.setNom(nom);
            p.setCorreo(correo);
            p.setTelefono(tel);
            p.setClave(cla);
            int r = dao.Actualizar(p);
            if (r == 1) {
                JOptionPane.showMessageDialog(vista, "Usuario Actualizado con Exito.");
            } else {
                JOptionPane.showMessageDialog(vista, "Error");
            }
        }
        limpiarTabla();
    }

    public void listar(JTable tabla) {
        centrarCeldas(tabla);
        modelo = (DefaultTableModel) tabla.getModel();
        tabla.setModel(modelo);
        List<Persona> lista = dao.listar();
        Object[] objeto = new Object[5];
        for (int i = 0; i < lista.size(); i++) {
            objeto[0] = lista.get(i).getId();
            objeto[1] = lista.get(i).getNom();
            objeto[2] = lista.get(i).getCorreo();
            objeto[3] = lista.get(i).getTelefono();
            objeto[4] = lista.get(i).getClave();
            modelo.addRow(objeto);
        }
        tabla.setRowHeight(35);
        tabla.setRowMargin(10);

    }

    void centrarCeldas(JTable tabla) {
        DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();
        tcr.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < vista.tabla.getColumnCount(); i++) {
            tabla.getColumnModel().getColumn(i).setCellRenderer(tcr);
        }
    }

    void limpiarTabla() {
        for (int i = 0; i < vista.tabla.getRowCount(); i++) {
            modelo.removeRow(i);
            i = i - 1;
        }
    }
public void Login() {
    String nom = login.txtNom.getText();
    String cla = login.txtClave.getText();
    p.setNom(nom);
    p.setClave(cla);
    int r = dao.login(p);
    
    if (r > 0) {
        JOptionPane.showMessageDialog(login, "Inicio de sesión exitoso.");
        menu.nombreUsuario = nom; // Asignar el nombre de usuario a la variable estática
        menu.setNombreUsuario(menu.nombreUsuario);
        menu.setVisible(true);
        login.setVisible(false);
    } else {
        JOptionPane.showMessageDialog(login, "Nombre de usuario o clave incorrectos.");
    }
}

}

