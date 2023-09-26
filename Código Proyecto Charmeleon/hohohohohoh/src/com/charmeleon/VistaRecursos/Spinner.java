package com.charmeleon.VistaRecursos;

import javax.swing.*;
import java.awt.*;
import java.util.Calendar;
import java.util.Date;

public class Spinner {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Date Spinner Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Crear el SpinnerDateModel con los límites y el valor inicial
        SpinnerDateModel dateModel = new SpinnerDateModel();
        dateModel.setCalendarField(Calendar.DAY_OF_MONTH); // Establecer el campo de calendario (día, mes o año)

        // Crear el JSpinner y asignar el modelo
        JSpinner spnFecha = new JSpinner(dateModel);

        // Personalizar el aspecto del spinner
        JSpinner.DateEditor editor = new JSpinner.DateEditor(spnFecha, "dd/MM/yyyy"); // Formato de fecha
        spnFecha.setEditor(editor);
        spnFecha.setPreferredSize(new Dimension(120, 25)); // Tamaño del spinner

        // Agregar el spinner al contenedor principal
        JPanel panel = new JPanel();
        panel.add(spnFecha);
        frame.add(panel);

        frame.pack();
        frame.setVisible(true);
    }
}
