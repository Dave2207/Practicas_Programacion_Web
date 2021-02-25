package Practica_CRUD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Tienda {
    private ArrayList<ventasProductos> listaVentas;
    private ArrayList<Usuario> listaUsuarios;
    private ArrayList<Producto> listaProductos;

    private static Tienda tienda = null;

    private Tienda() {
        this.listaVentas = new ArrayList<>();
        this.listaUsuarios = new ArrayList<>();
        this.listaProductos = new ArrayList<>();
    }

    public static Tienda getInstance(){
        if(tienda == null){
            tienda = new Tienda();
        }
        return tienda;
    }

    public ArrayList<ventasProductos> getListaVentas() {
        return listaVentas;
    }

    public void setListaVentas(ArrayList<ventasProductos> listaVentas) {
        this.listaVentas = listaVentas;
    }

    public ArrayList<Usuario> getListaUsuarios() {
        return listaUsuarios;
    }

    public void setListaUsuarios(ArrayList<Usuario> listaUsuarios) {
        this.listaUsuarios = listaUsuarios;
    }

    public ArrayList<Producto> getListaProductos() {
        return listaProductos;
    }

    public void setListaProductos(ArrayList<Producto> listaProductos) {
        this.listaProductos = listaProductos;
    }

    //Metodos de registro en listas
    public void agregarUsuario(Usuario usr){
        listaUsuarios.add(usr);
    }
    public void eliminarUsuario(Usuario usr){
        listaUsuarios.remove(usr);
    }
    public void agregarProducto(Producto prod){
        listaProductos.add(prod);
    }
    public void eliminarProducto(Producto prod){
        listaProductos.remove(prod);
    }
    public void registrarVenta(ventasProductos venta){
        listaVentas.add(venta);
    }

    public Usuario autenticarUsuario(String nombre, String pass){
        Usuario aux = null;
        boolean found = false;
        int i = 0;
        while(!found && i<listaUsuarios.size()){
            if(listaUsuarios.get(i).getNombre().equals(nombre) && listaUsuarios.get(i).getPassword().equals(pass)){
                aux = listaUsuarios.get(i);
                found = true;
            }
            i++;
        }
        return aux;
    }

    public void actualizarProducto(int id, String nombre, double precio){
        boolean found = false;
        int i = 0;
        while(!found && i<listaProductos.size()){
            if(listaProductos.get(i).getId() == id){
                listaProductos.get(i).setNombre(nombre);
                listaProductos.get(i).setPrecio(precio);
                found = true;
            }
            i++;
        }
    }

    public Producto findProductoById(int id){
        boolean found = false;
        int i = 0;
        Producto aux = null;
        while(!found && i<listaProductos.size()){
            if(listaProductos.get(i).getId() == id){
                aux = listaProductos.get(i);
                found = true;
            }
            i++;
        }
        return aux;
    }

	public void agregarVenta(ventasProductos venta) {
        listaVentas.add(venta);
	}

    public List<Usuario> cargarUsuariosDB(){
        List<Usuario> lista = new ArrayList<>();
        Connection con = null;
        try {
            String query = "select * from Usuarios";
            con = BaseDatos.getConnection();
            PreparedStatement pstmt = con.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();

            while(rs.next()){
                String username = rs.getString("username");
                String nombre = rs.getString("nombre");
                String pass = rs.getString("password");
                Usuario usr = new Usuario(username, nombre, pass);
                lista.add(usr);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Problemas en iniciar la conexión");
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                System.out.println("Hubo un problema al cerrar la conexión.");
            }
        }
        return lista;
    }

    public List<Producto> cargarProductosDB(){
        List<Producto> lista = new ArrayList<>();
        Connection con = null;
        try{
            String query = "select * from Productos";
            con = BaseDatos.getConnection();
            PreparedStatement pstmt = con.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();

            while(rs.next()){
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                Double precio = rs.getDouble("precio");
                Producto p = new Producto(id, nombre, precio);
                lista.add(p);
            }

        } catch(SQLException e){
            e.printStackTrace();
            System.out.println("Problemas en iniciar la conexión");
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                System.out.println("Hubo un problema al cerrar la conexión.");
            }
        }
        return lista;
    }

    public List<ventasProductos> cargarVentasDB(){
        List<ventasProductos> lista = new ArrayList<>();
        Connection con = null;
        try{
            String query = "select * from Ventas";
            con = BaseDatos.getConnection();
            PreparedStatement pstmt = con.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();

            while(rs.next()){
                String id = rs.getString("id");
                String fechaCompra = rs.getString("fechaCompra");
                String cliente = rs.getString("nombreCliente");
                Double total = rs.getDouble("total");
                // ventasProductos venta = new ventasProductos(id, fechaCompra, cliente, listaProductos);
                // lista.add(venta);
            }

        } catch(SQLException e){
            e.printStackTrace();
            System.out.println("Problemas en iniciar la conexión");
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                System.out.println("Hubo un problema al cerrar la conexión.");
            }
        }
        return lista;
    }
}
