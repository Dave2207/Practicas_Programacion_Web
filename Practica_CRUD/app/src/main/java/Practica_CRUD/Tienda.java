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
}
