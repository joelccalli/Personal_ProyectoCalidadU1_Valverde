/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.charmeleon.modeloDAO;

import com.charmeleon.modelo.Conexion;
import com.charmeleon.modelo.Productos;
import java.awt.Dimension;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 *
 * @author PC
 */
public class ProductosDAO {
    PreparedStatement ps;
    ResultSet rs;
    Connection con;
    Conexion conectar = new Conexion();
    Productos pr = new Productos();
public List<Productos> listarprod() {
    List<Productos> datos = new ArrayList<>();
    try {
        con = conectar.getConnection();
        ps = con.prepareStatement("SELECT Id, Nombre, Descripcion, Precio, Cantidad, Categoria FROM tbproducto");
        rs = ps.executeQuery();
        while (rs.next()) {
            Productos pr = new Productos();
            pr.setId(rs.getInt("Id"));
            pr.setNombre(rs.getString("Nombre"));
            pr.setDescripcion(rs.getString("Descripcion"));
            pr.setPrecio(rs.getDouble("Precio"));
            pr.setCantidad(rs.getInt("Cantidad"));
            pr.setCategoria(rs.getString("Categoria"));
            datos.add(pr);
        }
        
        
    } catch (Exception e) {
        e.printStackTrace();
    }
    return datos;
}
public int AgregarProd(Productos producto) {
    int r = 0;
    String sqlSelect = "SELECT COUNT(*) FROM tbproducto WHERE Id = ?";
    String sqlInsert = "INSERT INTO tbproducto (Id, Nombre, Descripcion, Precio, Cantidad, Categoria) VALUES (?, ?, ?, ?, ?, ?)";

    PreparedStatement psSelect = null;
    PreparedStatement psInsert = null;
    ResultSet rs = null;

    try {
        con = conectar.getConnection();

        psSelect = con.prepareStatement(sqlSelect);
        psSelect.setInt(1, producto.getId());
        rs = psSelect.executeQuery();

        if (rs.next()) {
            int count = rs.getInt(1);

            if (count > 0) {
                return 0;
            }
        }

        psInsert = con.prepareStatement(sqlInsert);
        psInsert.setInt(1, producto.getId());
        psInsert.setString(2, producto.getNombre());
        psInsert.setString(3, producto.getDescripcion());
        psInsert.setDouble(4, producto.getPrecio());
        psInsert.setInt(5, producto.getCantidad());
        psInsert.setString(6, producto.getCategoria());

        r = psInsert.executeUpdate();

        if (r == 1) {
            return 1;
        } else {
            return 0;
        }
    } catch (SQLException e) {
        e.printStackTrace();
    } finally {
        try {
            if (rs != null) {
                rs.close();
            }
            if (psSelect != null) {
                psSelect.close();
            }
            if (psInsert != null) {
                psInsert.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    return r;
}
 public int EliminarProd(int id) {
    int r = 0;
    String sql = "DELETE FROM tbproducto WHERE Id = ?";

    try {
        con = conectar.getConnection();
        ps = con.prepareStatement(sql);
        ps.setInt(1, id);
        r = ps.executeUpdate();
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        try {
            if (ps != null) {
                ps.close();
            }
            if (con != null) {
                con.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    return r;
}
public Productos obtenerProductoPorId(int id) {
    Productos producto = null;
    try {
        con = conectar.getConnection();
        ps = con.prepareStatement("SELECT Id, Nombre, Descripcion, Precio, Cantidad, Categoria FROM tbproducto WHERE Id = ?");
        ps.setInt(1, id);
        rs = ps.executeQuery();
        if (rs.next()) {
            producto = new Productos();
            producto.setId(rs.getInt("Id"));
            producto.setNombre(rs.getString("Nombre"));
            producto.setDescripcion(rs.getString("Descripcion"));
            producto.setPrecio(rs.getDouble("Precio"));
            producto.setCantidad(rs.getInt("Cantidad"));
            producto.setCategoria(rs.getString("Categoria"));
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return producto;
}
public int ActualizarProd(Productos producto) {
    int resultado = 0;
    try {
        con = conectar.getConnection();
        String sql = "UPDATE tbproducto SET Nombre = ?, Descripcion = ?, Precio = ?, Cantidad = ?, Categoria = ? WHERE Id = ?";
        ps = con.prepareStatement(sql);
        ps.setString(1, producto.getNombre());
        ps.setString(2, producto.getDescripcion());
        ps.setDouble(3, producto.getPrecio());
        ps.setInt(4, producto.getCantidad());
        ps.setString(5, producto.getCategoria());
        ps.setInt(6, producto.getId());

        resultado = ps.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
    } finally {
        try {
            if (ps != null) {
                ps.close();
            }
            if (con != null) {
                con.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    return resultado;
}
}
