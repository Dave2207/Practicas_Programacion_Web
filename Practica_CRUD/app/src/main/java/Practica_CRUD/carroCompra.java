package Practica_CRUD;

import java.util.ArrayList;

public class carroCompra {
    private int id;
    private ArrayList<Producto> listaProductos;

    public carroCompra(int id, ArrayList<Producto> listaProductos) {
        this.id = id;
        this.listaProductos = listaProductos;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<Producto> getListaProductos() {
        return listaProductos;
    }

    public void setListaProductos(ArrayList<Producto> listaProductos) {
        this.listaProductos = listaProductos;
    }

}
