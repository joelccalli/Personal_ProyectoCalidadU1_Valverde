package com.charmeleon.modeloDAO;

import com.charmeleon.modelo.Boleta;
import com.charmeleon.modelo.BoletaDetalle;
import com.charmeleon.modelo.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BoletaDAO {
    PreparedStatement ps;
    ResultSet rs;
    Connection con;
    Conexion conectar = new Conexion();

    public List<Boleta> listarBoletas() {
        List<Boleta> datos = new ArrayList<>();
        try {
            con = conectar.getConnection();
            ps = con.prepareStatement("SELECT NBoleta, Cliente, Dni, FechaE, Hora FROM tbboleta");
            rs = ps.executeQuery();
            while (rs.next()) {
                Boleta boleta = new Boleta();
                boleta.setNBoleta(rs.getInt("NBoleta"));
                boleta.setCliente(rs.getString("Cliente"));
                boleta.setDni(rs.getInt("Dni"));
                boleta.setFechaE(rs.getDate("FechaE"));
                boleta.setHora(rs.getString("Hora"));
                datos.add(boleta);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return datos;
    }


    public int agregarBoleta(Boleta boleta) {
        int resultado = 0;
        String sqlInsert = "INSERT INTO tbboleta (NBoleta, Cliente, Dni, FechaE, Hora) VALUES (?, ?, ?, ?, ?)";

        try {
            con = conectar.getConnection();

            ps = con.prepareStatement(sqlInsert);
            ps.setInt(1, boleta.getNBoleta());
            ps.setString(2, boleta.getCliente());
            ps.setInt(3, boleta.getDni());
            ps.setDate(4, new java.sql.Date(boleta.getFechaE().getTime()));
            ps.setString(5, boleta.getHora());

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


    public int eliminarBoleta(int NBoleta) {
        int resultado = 0;
        String sql = "DELETE FROM tbboleta WHERE NBoleta = ?";
        try {
            con = conectar.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, NBoleta);
            resultado = ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultado;
    }

    public Boleta obtenerBoletaPorNBoleta(int NBoleta) {
        Boleta boleta = null;
        try {
            con = conectar.getConnection();
            ps = con.prepareStatement("SELECT NBoleta, Cliente, Dni, FechaE, Hora FROM tbboleta WHERE NBoleta = ?");
            ps.setInt(1, NBoleta);
            rs = ps.executeQuery();
            if (rs.next()) {
                boleta = new Boleta();
                boleta.setNBoleta(rs.getInt("NBoleta"));
                boleta.setCliente(rs.getString("Cliente"));
                boleta.setDni(rs.getInt("Dni"));
                boleta.setFechaE(rs.getDate("FechaE"));
                boleta.setHora(rs.getString("Hora"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return boleta;
    }
    public void insertarBoletaDetalle(BoletaDetalle boletaDetalle) {
    String sqlInsertDetalle = "INSERT INTO tbboletadetalle (Producto, Descripcion, Cantidad, Moneda, Precio, Subtotal, Total, fkboleta) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?)";

    try {
        con = conectar.getConnection();

        ps = con.prepareStatement(sqlInsertDetalle);
        ps.setInt(1, boletaDetalle.getProducto());
        ps.setString(2, boletaDetalle.getDescripcion());
        ps.setInt(3, boletaDetalle.getCantidad());
        ps.setString(4, boletaDetalle.getMoneda());
        ps.setDouble(5, boletaDetalle.getPrecio());
        ps.setDouble(6, boletaDetalle.getSubtotal());
        ps.setDouble(7, boletaDetalle.getTotal());
        ps.setInt(8,boletaDetalle.getFkboelta());

        ps.executeUpdate();
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
}


    public int actualizarBoleta(Boleta boleta) {
        int resultado = 0;
        try {
            con = conectar.getConnection();
            String sql = "UPDATE tbboleta SET Cliente = ?, Dni = ?, FechaE = ?, Hora = ? WHERE NBoleta = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, boleta.getCliente());
            ps.setInt(2, boleta.getDni());
            ps.setDate(3, new java.sql.Date(boleta.getFechaE().getTime()));
            ps.setString(4, boleta.getHora());
            ps.setInt(5, boleta.getNBoleta());

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
