package com.charmeleon.modelo;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
public class Conexion {

    public static void close(PreparedStatement statement) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    String url="jdbc:mysql://localhost:3306/bdcharmelon";
    String user="root",pass="";    
    Connection con;
    public Connection getConnection(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con=DriverManager.getConnection(url,user,pass);
        } catch (Exception e) {            
        }
        return con;
    }
}
