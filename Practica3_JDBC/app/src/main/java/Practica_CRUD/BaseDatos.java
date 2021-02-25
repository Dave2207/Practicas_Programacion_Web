package Practica_CRUD;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.h2.tools.Server;

public class BaseDatos {
    private static BaseDatos db = null;
    private static String url = "jdbc:h2:tcp://localhost/~/dbCRUD";
    private static Server server;

    private BaseDatos(){

    }

    public static BaseDatos getInstance(){
        if(db == null){
            db = new BaseDatos();
        }
        return db;
    }

    public static Connection getConnection(){
        Connection con = null;
        try{
            Class.forName("org.h2.Driver");
            con = DriverManager.getConnection(url, "user", "password");
            System.out.println("Conexión establecida.");
        } catch(SQLException | ClassNotFoundException e){
            System.out.println("Ha habido un error con la conexión.");
            e.printStackTrace();
        }
        return con;
    }

    public static void startDB() throws SQLException {
        server = Server.createTcpServer("-tcpPort", "9092", "-tcpAllowOthers", "-ifNotExists").start();
    }

    public static void stopDB() throws SQLException {
        server.shutdown();
    }
    
}
