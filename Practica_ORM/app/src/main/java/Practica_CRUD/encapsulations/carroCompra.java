package Practica_CRUD.encapsulations;

import java.util.ArrayList;

public class carroCompra {
    private String id;
    private ArrayList<Producto> listaProductos;

    public carroCompra(String id) {
        this.id = id;
        this.listaProductos = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<Producto> getListaProductos() {
        return listaProductos;
    }

    public void setListaProductos(ArrayList<Producto> listaProductos) {
        this.listaProductos = listaProductos;
    }

    public void agregarProducto(Producto prod){
        listaProductos.add(prod);
    }
    public void eliminarProducto(Producto prod){
        listaProductos.remove(prod);
    }

}
