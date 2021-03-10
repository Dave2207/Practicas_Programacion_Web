package Practica_CRUD.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

    public Connection getConnection(){
        Connection con = null;
        try{
            Class.forName("org.h2.Driver");
            con = DriverManager.getConnection(url, "sa", "");
            System.out.println("Conexión establecida.");
        } catch(SQLException | ClassNotFoundException e){
            System.out.println("Ha habido un error con la conexión.");
            e.printStackTrace();
        }
        return con;
    }

    public void startDB() {
        try {
        server = Server.createTcpServer("-tcpPort", "9092", "-tcpAllowOthers", "-tcpDaemon","-ifNotExists").start();
        } catch(SQLException e){
            e.getMessage();
        }
    }

    public void stopDB() throws SQLException {
        server.shutdown();
    }
    
    // public static void crearTablas() throws SQLException{
    //     String create = "create table if not exists ";
    //     Connection con = getConnection();
    //     Statement stmt = con.createStatement();
        
    //     //Tabla productos
    //     stmt.execute(create + "Productos(\n"+
    //     "id int primary key identity(1,1), \n"+
    //     "nombre varchar(40), \n"+
    //     "precio Decimal)\n"
    //     +")");
    //     //Tabla Usuarios
    //     stmt.execute(create + "Usuarios(\n"+
    //     "username varchar(30) primary key, \n"+
    //     "nombre varchar(50), \n"+
    //     "password varchar(40)\n"
    //     +")");
    //     //Tabla ventas
    //     stmt.execute(create + "Ventas(\n"+
    //     "id varchar(20) primary key identity(1,1), \n"+
    //     "fechaCompra varchar(35), \n"+
    //     "nombreCliente varchar(50), \n"+
    //     "total decimal\n"
    //     +")");

    //     stmt.close();
    //     con.close();
        
    // }

    // //Queries de creacion de objetos
    // public boolean crearProducto(Producto p){
    //     boolean ok = false;
    //     Connection con = null;
    //     try {
    //         String query = "insert into Productos(nombre, precio) values (?,?)";
    //         con = getConnection();
    //         PreparedStatement stmt = con.prepareStatement(query);
    //         //Setear los atributos de objeto
    //         stmt.setString(1, p.getNombre());
    //         stmt.setDouble(2, p.getPrecio());

    //         int fila = stmt.executeUpdate();
    //         ok = fila > 0;
    //     } catch (SQLException e) {
    //         e.printStackTrace();
    //         System.out.println("Error de conexión");
    //     } finally{
    //         try{
    //             con.close();
    //         } catch (SQLException e){
    //             e.printStackTrace();
    //             System.out.println("Error al cerrar conexión");
    //         }
    //     }
    //     return ok;
    // }

    // public boolean crearUsuario(Usuario usr){
    //     boolean ok = false;
    //     Connection con = null;

    //     try {
    //         String query = "insert into Usuarios(username, nombre, password) values (?,?,?)";
    //         con = getConnection();
    //         PreparedStatement stmt = con.prepareStatement(query);
    //         //Setear parametros
    //         stmt.setString(1, usr.getNombre());
    //         stmt.setString(2, usr.getUsuario());
    //         stmt.setString(3, usr.getPassword());

    //         int fila = stmt.executeUpdate();
    //         ok = fila > 0;
    //     } catch (SQLException e) {
    //         e.printStackTrace();
    //         System.out.println("Error de conexión");
    //     } finally{
    //         try {
    //             con.close();
    //         } catch (SQLException e) {
    //             e.printStackTrace();
    //             System.out.println("Error al cerrar conexión");
    //         }
    //     }
    //     return ok;
    // }

    // public boolean crearVenta(ventasProductos venta){
    //     boolean ok = false;
    //     Connection con = null;

    //     try {
    //         String query = "insert into Ventas(fechaCompra, nombreCliente, total) values (?,?,?)";
    //         con = getConnection();
    //         PreparedStatement stmt = con.prepareStatement(query);
    //         //Setear parametros
    //         stmt.setString(1, venta.getFechaCompra());
    //         stmt.setString(2, venta.getNombreCliente());
    //         stmt.setDouble(3, venta.getTotal());

    //         int fila = stmt.executeUpdate();
    //         ok = fila > 0;
    //     } catch (SQLException e) {
    //         e.printStackTrace();
    //         System.out.println("Error de conexión");
    //     } finally{
    //         try {
    //             con.close();
    //         } catch (SQLException e) {
    //             e.printStackTrace();
    //             System.out.println("Error al cerrar conexión");
    //         }
    //     }
    //     return ok;
    // }

    // //Queries de modificacion de objeto
    // public boolean editarProductoDB(Producto p){
    //     boolean ok = false;
    //     Connection con = null;
    //     try {
    //         String query = "update Productos set nombre=?, precio=? where id=?";
    //         con = getConnection();
    //         PreparedStatement stmt = con.prepareStatement(query);
    //         stmt.setString(1, p.getNombre());
    //         stmt.setDouble(2, p.getPrecio());
    //         stmt.setInt(3, p.getId());

    //         int fila = stmt.executeUpdate();
    //         ok = fila > 0;

    //     } catch (SQLException e) {
    //         e.printStackTrace();
    //         System.out.println("Error de conexión");
    //     } finally{
    //         try {
    //             con.close();
    //         } catch (SQLException e) {
    //             e.printStackTrace();
    //             System.out.println("Error al cerrar conexión");
    //         }
    //     }
    //     return ok;
    // }

    // public boolean editarUsuarioDB(Usuario usr){
    //     boolean ok = false;
    //     Connection con = null;
    //     try {
    //         String query = "update Usuarios set nombre=?, password=? where username=?";
    //         con = getConnection();
    //         PreparedStatement stmt = con.prepareStatement(query);
    //         stmt.setString(1, usr.getUsuario());
    //         stmt.setString(2, usr.getPassword());
    //         stmt.setString(3, usr.getNombre());

    //         int fila = stmt.executeUpdate();
    //         ok = fila > 0;

    //     } catch (SQLException e) {
    //         e.printStackTrace();
    //         System.out.println("Error de conexión");
    //     } finally{
    //         try {
    //             con.close();
    //         } catch (SQLException e) {
    //             e.printStackTrace();
    //             System.out.println("Error al cerrar conexión");
    //         }
    //     }
    //     return ok;
    // }

    // //Queries de borrado
    // public boolean eliminarProductoDB(int id){
    //     boolean ok = false;
    //     Connection con = null;
    //     try {
    //         String query = "delete from Productos where id = ?";
    //         con = getConnection();
    //         PreparedStatement stmt = con.prepareStatement(query);
    //         stmt.setInt(1, id);

    //         int fila = stmt.executeUpdate();
    //         ok = fila > 0;
    //     } catch (SQLException e) {
    //         e.printStackTrace();
    //         System.out.println("Error de conexión");
    //     } finally{
    //         try {
    //             con.close();
    //         } catch (SQLException e) {
    //             e.printStackTrace();
    //             System.out.println("Error al cerrar conexión");
    //         }
    //     }
    //     return ok;
    // }

    // public boolean eliminarUsuarioDB(String username){
    //     boolean ok = false;
    //     Connection con = null;
    //     try {
    //         String query = "delete from Usuarios where username = ?";
    //         con = getConnection();
    //         PreparedStatement stmt = con.prepareStatement(query);
    //         stmt.setString(1, username);

    //         int fila = stmt.executeUpdate();
    //         ok = fila > 0;
    //     } catch (SQLException e) {
    //         e.printStackTrace();
    //         System.out.println("Error de conexión");
    //     } finally{
    //         try {
    //             con.close();
    //         } catch (SQLException e) {
    //             e.printStackTrace();
    //             System.out.println("Error al cerrar conexión");
    //         }
    //     }
    //     return ok;
    // }

    
}
