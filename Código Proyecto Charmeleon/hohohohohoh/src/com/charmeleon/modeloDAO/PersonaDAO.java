package com.charmeleon.modeloDAO;

import com.charmeleon.modelo.Conexion;
import com.charmeleon.modelo.Persona;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PersonaDAO {

    PreparedStatement ps;
    ResultSet rs;
    Connection con;
    Conexion conectar = new Conexion();
    Persona p = new Persona();

   public List<Persona> listar() {
    List<Persona> datos = new ArrayList<>();
    try {
        con = conectar.getConnection();
        ps = con.prepareStatement("SELECT Id, Nombres, Correo, Telefono, '***' AS Clave FROM tbempleado");
        rs = ps.executeQuery();
        while (rs.next()) {
            Persona p = new Persona();
            p.setId(rs.getInt("Id"));
            p.setNom(rs.getString("Nombres"));
            p.setCorreo(rs.getString("Correo"));
            p.setTelefono(rs.getString("Telefono"));
            p.setClave(rs.getString("Clave"));
            datos.add(p);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return datos;
}

public int agregar(Persona per) {  
    int r = 0;
    String sqlSelect = "SELECT COUNT(*) FROM tbempleado WHERE Id = ?";
    String sqlInsert = "INSERT INTO tbempleado (Id, Nombres, Correo, Telefono, Clave) VALUES (?, ?, ?, ?, ?)";
    
    PreparedStatement psSelect = null;
    PreparedStatement psInsert = null;
    
    try {
        con = conectar.getConnection();
        
     
        psSelect = con.prepareStatement(sqlSelect);
        psSelect.setInt(1, per.getId());
        ResultSet rs = psSelect.executeQuery();
        
        if (rs.next()) {
            int count = rs.getInt(1);
            
            if (count > 0) {
         
                return 0;
            }
        }
        
     
        psInsert = con.prepareStatement(sqlInsert);
        psInsert.setInt(1, per.getId());
        psInsert.setString(2, per.getNom());
        psInsert.setString(3, per.getCorreo());
        psInsert.setString(4, per.getTelefono());
        psInsert.setString(5, per.getClave());
        
        r = psInsert.executeUpdate();    
        
        if (r == 1) {
            return 1;
        } else {
            return 0;
        }
    } catch (Exception e) {
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



public int Actualizar(Persona per) {  
        int r=0;
        String sql="UPDATE tbempleado SET Nombres=?,Correo=?,Telefono=?, Clave=? where Id=?";        try {
            con = conectar.getConnection();
            ps = con.prepareStatement(sql);            
            ps.setString(1,per.getNom());
            ps.setString(2,per.getCorreo());
            ps.setString(3,per.getTelefono());
            ps.setString(4,per.getClave());
            ps.setString(5,String.valueOf(per.getId()));

            
            r=ps.executeUpdate();    
            if(r==1){
                return 1;
            }
            else{
                return 0;
            }
        } catch (Exception e) {
        }  
        return r;
    }


    public int Delete(int id){
        int r=0;
        String sql="delete from tbempleado where Id="+id;
        try {
            con=conectar.getConnection();
            ps=con.prepareStatement(sql);
            r= ps.executeUpdate();
        } catch (Exception e) {
        }
        return r;
    }
public int login(Persona per) {
    String sql = "SELECT COUNT(*) FROM tbempleado WHERE Nombres = ? AND Clave = ?";
    try {
        con = conectar.getConnection();
        ps = con.prepareStatement(sql);
        ps.setString(1, per.getNom());
        ps.setString(2, per.getClave());
        rs = ps.executeQuery();

        if (rs.next()) {
            return rs.getInt(1);
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

    return 0;
}




}
