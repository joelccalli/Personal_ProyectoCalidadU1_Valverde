/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.charmeleon.modeloDAO;

import com.charmeleon.modelo.Conexion;
import com.charmeleon.modelo.Cargo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CargoDAO {
       PreparedStatement ps;
     ResultSet rs;
     Connection con;
     Conexion conectar = new Conexion();

   public List<Cargo> listarCargo() {
    List<Cargo> datos = new ArrayList<>();
    try {
        con = conectar.getConnection();
        ps = con.prepareStatement("SELECT Id, Nombre, Descripcion FROM tbcargo");
        rs = ps.executeQuery();
        while (rs.next()) {
            Cargo cargo = new Cargo();
            cargo.setId(rs.getInt("Id"));
            cargo.setNombre(rs.getString("Nombre"));
            cargo.setDescripcion(rs.getString("Descripcion"));
            datos.add(cargo);
        }
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        try {
            if (rs != null) {
                rs.close();
            }
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
    return datos;
}


    public int agregarCargo(Cargo cargo) {
        int resultado = 0;
        try {
            con = conectar.getConnection();
            String sql = "INSERT INTO tbcargo (Id, Nombre, Descripcion) VALUES (?, ?, ?)";
            ps = con.prepareStatement(sql);
            ps.setInt(1, cargo.getId());
            ps.setString(2, cargo.getNombre());
            ps.setString(3, cargo.getDescripcion());
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

    public int eliminarCargo(int id) {
        int resultado = 0;
        try {
            con = conectar.getConnection();
            String sql = "DELETE FROM tbcargo WHERE Id = ?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            resultado = ps.executeUpdate();
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
        return resultado;
    }

    public Cargo obtenerCargoPorId(int id) {
        Cargo cargo = null;
        try {
            con = conectar.getConnection();
            ps = con.prepareStatement("SELECT Id, Nombre, Descripcion FROM tbcargo WHERE Id = ?");
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                cargo = new Cargo();
                cargo.setId(rs.getInt("Id"));
                cargo.setNombre(rs.getString("Nombre"));
                cargo.setDescripcion(rs.getString("Descripcion"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
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
        return cargo;
    }

    public int actualizarCargo(Cargo cargo) {
        int resultado = 0;
        try {
            con = conectar.getConnection();
            String sql = "UPDATE tbcargo SET Nombre = ?, Descripcion = ? WHERE Id = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, cargo.getNombre());
            ps.setString(2, cargo.getDescripcion());
            ps.setInt(3, cargo.getId());
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
