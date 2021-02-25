package Practica_CRUD;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

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
            con = DriverManager.getConnection(url, "David", "12345");
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
    
    public static void crearTablas() throws SQLException{
        String create = "create table if not exists ";
        Connection con = getConnection();
        Statement stmt = con.createStatement();
        
        //Tabla productos
        stmt.execute(create + "Productos(\n"+
        "id int primary key identity(1,1), \n"+
        "nombre varchar(40), \n"+
        "precio Decimal)\n"
        +")");
        //Tabla Usuarios
        stmt.execute(create + "Usuarios(\n"+
        "username varchar(30) primary key, \n"+
        "nombre varchar(50), \n"+
        "password varchar(40)\n"
        +")");
        //Tabla ventas
        stmt.execute(create + "Ventas(\n"+
        "id varchar(20) primary key, \n"+
        "fechaCompra varchar(35), \n"+
        "nombreCliente varchar(50), \n"+
        "total decimal\n"
        +")");

        stmt.close();
        con.close();
        
    }
}
