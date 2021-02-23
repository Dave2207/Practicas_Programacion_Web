package Practica_CRUD;

import java.util.ArrayList;

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

    public boolean autenticarUsuario(String nombre, String pass){
        boolean found = false;
        int i = 0;
        while(!found && i<listaUsuarios.size()){
            if(listaUsuarios.get(i).getNombre().equals(nombre) && listaUsuarios.get(i).getPassword().equals(pass)){
                found = true;
            }
            i++;
        }
        return found;
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
}
