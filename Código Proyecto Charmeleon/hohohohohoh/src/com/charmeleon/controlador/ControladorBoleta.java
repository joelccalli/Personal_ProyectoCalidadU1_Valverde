package com.charmeleon.controlador;



import com.charmeleon.modelo.Boleta;
import com.charmeleon.modelo.BoletaDetalle;
import com.charmeleon.modelo.Productos;
import com.charmeleon.modeloDAO.BoletaDAO;
import com.charmeleon.modeloDAO.ProductosDAO;
import com.charmeleon.vista.FrmBoletas;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.SpinnerDateModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableCellRenderer;

public class ControladorBoleta implements ActionListener {
    BoletaDAO boletaDAO;
    FrmBoletas boletasView;
    Boleta bol = new Boleta();
    BoletaDetalle boldet = new BoletaDetalle();
    DefaultTableModel modelo = new DefaultTableModel();
    ProductosDAO productosDAO;

    public ControladorBoleta(FrmBoletas boletasView) {
        this.boletasView = boletasView;
        this.boletaDAO = new BoletaDAO();
        this.boletasView.btnBuscarProducto.addActionListener(this);

        this.boletasView.btnAgregarBol.addActionListener(this);
        this.boletasView.btnAñadirTabla.addActionListener(this);
        this.productosDAO = new ProductosDAO();
        this.boletasView.btnAnular.addActionListener(this);
        this.boletasView.btnpdf.addActionListener(this);
        this.boletasView.spnCantidadDetBol.addChangeListener(new ChangeListener()
        {
            public void stateChanged(ChangeEvent e) {
                calcularSubtotal();
            }
        });
        this.boletasView.txtPrecioUndBol.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                calcularSubtotal();
            }
        });
mostrarBoletasEnTabla(); 
    }

    

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == boletasView.btnAgregarBol) {
            agregarBoleta();
        }
        if (e.getSource() == boletasView.btnBuscarProducto) {
            buscarProducto();
        }
        if (e.getSource() == boletasView.btnAñadirTabla) {
            agregarDetalleBoleta();
        }
        if (e.getSource() == boletasView.btnAnular) {
        eliminarFilaSeleccionada();
        }
      if (e.getSource() == boletasView.btnpdf) {
            generarPDF();
        }
    }
        

 public void agregarBoleta() {
    int nBoleta = Integer.parseInt(boletasView.txtNboleta.getText());
    String cliente = boletasView.txtClienteBol.getText();
    int dni = Integer.parseInt(boletasView.txtDniBol.getText());
    SpinnerDateModel dateModel = (SpinnerDateModel) boletasView.spnFecha.getModel();
    Date fechaE = dateModel.getDate();
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    String hora = boletasView.txtHoraBol.getText();
    String moneda = "";
    if (boletasView.rbtnSoles.isSelected()) {
        moneda = "Soles";
    } else if (boletasView.rbtnDolares.isSelected()) {
        moneda = "Dólares";
    } else if (boletasView.rbtnPesos.isSelected()) {
        moneda = "Pesos Cl";
    }
    String total = boletasView.txtTotDetBol.getText();

    int confirm = JOptionPane.showConfirmDialog(boletasView, "¿Está seguro que desea generar la boleta?\n" +
            "N° Boleta: " + nBoleta + "\n" +
            "Cliente: " + cliente + "\n" +
            "DNI: " + dni + "\n" +
            "Fecha: " + dateFormat.format(fechaE) + "\n" +
            "Hora: " + hora + "\n" +
            "Moneda: " + moneda + "\n" +
            "Total: " + total, "Confirmar agregación del producto", JOptionPane.YES_NO_OPTION);

   if (confirm == JOptionPane.YES_OPTION) {
    // Guardar los datos en la tabla tbboleta
    Boleta boleta = new Boleta();
    boleta.setNBoleta(nBoleta);
    boleta.setCliente(cliente);
    boleta.setDni(dni);
    boleta.setFechaE(fechaE);
    boleta.setHora(hora);
   
    boletaDAO.agregarBoleta(boleta);


    DefaultTableModel modelo = (DefaultTableModel) boletasView.boletasTable.getModel();
    int filas = modelo.getRowCount();
    for (int i = 0; i < filas; i++) {
    BoletaDetalle detalle = new BoletaDetalle();
    detalle.setFkboelta(nBoleta);
    detalle.setProducto(Integer.parseInt(modelo.getValueAt(i, 0).toString()));
    detalle.setDescripcion(modelo.getValueAt(i, 1).toString());
    detalle.setCantidad(Integer.parseInt(modelo.getValueAt(i, 2).toString()));
    detalle.setPrecio(Double.parseDouble(modelo.getValueAt(i, 3).toString()));
    detalle.setMoneda(moneda);
    detalle.setSubtotal(Double.parseDouble(modelo.getValueAt(i, 4).toString()));
    
    // Asegurarse de que el valor 'total' esté establecido correctamente
    double subtotal = Double.parseDouble(modelo.getValueAt(i, 4).toString());
    detalle.setTotal(subtotal); // Establecer el valor 'total' en el objeto BoletaDetalle
    
    boletaDAO.insertarBoletaDetalle(detalle);
}


    // Limpiar los campos de texto después de agregar la boleta
    boletasView.txtNboleta.setText("");
    boletasView.txtClienteBol.setText("");
    boletasView.txtDniBol.setText("");
    boletasView.txtHoraBol.setText("");
    boletasView.txtprodDetBol.setText("");
    boletasView.txtxDescDetBol.setText("");
    
    boletasView.spnCantidadDetBol.setValue(1);
    boletasView.rbtnSoles.setSelected(true);
    boletasView.txtPrecioUndBol.setText("");
    boletasView.txtSubDetBol.setText("");

    // Limpiar la tabla
    modelo.setRowCount(0);
     mostrarBoletasEnTabla();
}

}


   public void calcularSubtotal() {
    int cantidad = (int) boletasView.spnCantidadDetBol.getValue();
    double precio = Double.parseDouble(boletasView.txtPrecioUndBol.getText());

    // Obtener el valor máximo permitido desde txtCabtBusc
    int maxCantidad = Integer.parseInt(boletasView.txtCantBusc.getText());

    // Verificar si la cantidad ingresada excede el valor máximo permitido
    if (cantidad > maxCantidad) {
        cantidad = maxCantidad;
        boletasView.spnCantidadDetBol.setValue(cantidad);
    }

    double subtotal = cantidad * precio;
    boletasView.txtSubDetBol.setText(String.valueOf(subtotal));
}


    public void agregarDetalleBoleta() {
        String descripcion = boletasView.txtDescBusc.getText();
        int producto = Integer.parseInt(boletasView.txtprodDetBol.getText());
        int cantidad = (int) boletasView.spnCantidadDetBol.getValue();
        double precio = Double.parseDouble(boletasView.txtPrecioBusc.getText());
        double subtotal = Double.parseDouble(boletasView.txtSubDetBol.getText());

        
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        String precioFormatted = decimalFormat.format(precio);
        String subtotalFormatted = decimalFormat.format(subtotal);
 

      
        Object[] fila = new Object[7];
        fila[0] = producto;
        fila[1] = descripcion;
        fila[2] = cantidad;
        fila[3] = precioFormatted;
        fila[4] = subtotalFormatted;
       
        // Obtener el modelo de tabla
        DefaultTableModel modelo = (DefaultTableModel) boletasView.boletasTable.getModel();

        // Agregar la fila al modelo de tabla
        modelo.addRow(fila);
        
        centrarDatosEnTabla();
        // Sumar el nuevo subtotal al campo txtTotalBol
        double totalActual = Double.parseDouble(boletasView.txtTotDetBol.getText());
        double nuevoTotal = totalActual + subtotal;
        String nuevoTotalFormatted = decimalFormat.format(nuevoTotal);
        boletasView.txtTotDetBol.setText(nuevoTotalFormatted);

        // Limpiar los campos de texto después de agregar la fila
        boletasView.txtDescBusc.setText("");
        boletasView.spnCantidadDetBol.setValue(1);
        boletasView.txtPrecioBusc.setText("");
        boletasView.txtSubDetBol.setText("");
    }

    public void buscarProducto() {
        int idProducto = Integer.parseInt(boletasView.txtIdBuscarProd.getText());
        Productos producto = productosDAO.obtenerProductoPorId(idProducto); // Llamar al método desde ProductosDAO

        if (producto != null) {
            boletasView.txtDescBusc.setText(producto.getNombre());
            boletasView.txtCantBusc.setText(String.valueOf(producto.getCantidad()));
            boletasView.txtPrecioBusc.setText(String.valueOf(producto.getPrecio()));
            boletasView.txtPrecioUndBol.setText(String.valueOf(producto.getPrecio()));
            boletasView.txtxDescDetBol.setText(producto.getNombre());
            boletasView.txtprodDetBol.setText(String.valueOf(idProducto));
        } else {
            JOptionPane.showMessageDialog(boletasView, "Producto no encontrado");
        }
    }
    public void mostrarBoletasEnTabla() {
        // Obtener la lista de boletas
        List<Boleta> boletas = boletaDAO.listarBoletas();

        // Crear un modelo de tabla personalizado
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("NBoleta");
        modelo.addColumn("Cliente");
        modelo.addColumn("Dni");
        modelo.addColumn("FechaE");
        modelo.addColumn("Hora");

        // Agregar los datos de las boletas al modelo de tabla
        for (Boleta boleta : boletas) {
            Object[] fila = {
                boleta.getNBoleta(),
                boleta.getCliente(),
                boleta.getDni(),
                boleta.getFechaE(),
                boleta.getHora()
            };
            modelo.addRow(fila);
        }

        
        boletasView.nuevatablaboleta.setModel(modelo);
    }


    
    public void eliminarFilaSeleccionada() {
   
    int filaSeleccionada = boletasView.boletasTable.getSelectedRow();

    
    if (filaSeleccionada != -1) {
        DefaultTableModel modelo = (DefaultTableModel) boletasView.boletasTable.getModel();

     
        double subtotal = Double.parseDouble(modelo.getValueAt(filaSeleccionada, 4).toString());

        
        double totalActual = Double.parseDouble(boletasView.txtTotDetBol.getText());
        double nuevoTotal = totalActual - subtotal;
        boletasView.txtTotDetBol.setText(String.valueOf(nuevoTotal));

        modelo.removeRow(filaSeleccionada);
    } else {
        JOptionPane.showMessageDialog(boletasView, "No se ha seleccionado una fila");
    }
}
  public void generarPDF() {
    String numeroBoleta = boletasView.txtNboleta.getText();
    String filePath = System.getProperty("user.home") + "/Desktop/Boleta_" + numeroBoleta + ".pdf";
    String moneda1 = "";
    if (boletasView.rbtnSoles.isSelected()) {
        moneda1 = "Soles";
    } else if (boletasView.rbtnDolares.isSelected()) {
        moneda1 = "Dólares";
    } else if (boletasView.rbtnPesos.isSelected()) {
        moneda1 = "Pesos Cl";
    }
    try {
        // Crear el documento PDF
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(filePath));
        document.open();

        // Crear el título de la boleta
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, Font.UNDERLINE);
        Paragraph title = new Paragraph("Boleta " + numeroBoleta, titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        Paragraph name = new Paragraph("CHARMELEON");
        name.setAlignment(Element.ALIGN_CENTER);
        document.add(name);
        // Agregar el espacio entre el título y los datos
        document.add(new Paragraph("\n+\n"));

        // Agregar los valores de los campos
        Paragraph fields = new Paragraph();
        fields.add("Cliente: " + boletasView.txtClienteBol.getText() + "\n");
        fields.add("DNI: " + boletasView.txtDniBol.getText() + "\n");
        fields.add("Fecha: " + boletasView.spnFecha.getValue().toString() + "\n");
        fields.add("Hora: " + boletasView.txtHoraBol.getText() + "\n");
        fields.add("Moneda: " + moneda1 + "\n");
        
        document.add(fields);

        
        document.add(new Paragraph("\n"));

   
        

        
        document.add(new Paragraph("\n"));

        // Crear la tabla
        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);

        // Agregar las cabeceras de la tabla
        table.addCell("Producto");
        table.addCell("Descripción");
        table.addCell("Cantidad");
        table.addCell("Precio");
        table.addCell("Subtotal");

        // Obtener el modelo de la tabla
        DefaultTableModel modelo = (DefaultTableModel) boletasView.boletasTable.getModel();

        // Agregar los datos de la tabla al documento PDF
        int filas = modelo.getRowCount();
        for (int i = 0; i < filas; i++) {
            String producto = modelo.getValueAt(i, 0).toString();
            String descripcion = modelo.getValueAt(i, 1).toString();
            String cantidad = modelo.getValueAt(i, 2).toString();
            String precio = modelo.getValueAt(i, 3).toString();
            String subtotal = modelo.getValueAt(i, 4).toString();

            table.addCell(producto);
            table.addCell(descripcion);
            table.addCell(cantidad);
            table.addCell(precio);
            table.addCell(subtotal);
        }

        
        document.add(table);
            Paragraph total = new Paragraph("TOTAL: " + boletasView.txtTotDetBol.getText());
             total.setAlignment(Element.ALIGN_RIGHT);
             document.add(total);
        // Cerrar el documento
        document.close();

        JOptionPane.showMessageDialog(boletasView, "Se ha generado el PDF correctamente");

    } catch (Exception ex) {
        JOptionPane.showMessageDialog(boletasView, "Error al generar el PDF: " + ex.getMessage());
    }
}

    public void centrarDatosEnTabla() {
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        TableColumnModel columnModel = boletasView.boletasTable.getColumnModel();
        for (int i = 0; i < columnModel.getColumnCount(); i++) {
            columnModel.getColumn(i).setCellRenderer(centerRenderer);
        }
     
    }

}
