package Practica_CRUD;

import java.util.ArrayList;
import java.util.Date;

public class ventasProductos {
    private String id;
    private String fechaCompra;
    private String nombreCliente;
    private ArrayList<Producto> listaProductos;

    public ventasProductos(String id, String fechaCompra, String nombreCliente, ArrayList<Producto> listaProductos) {
        this.id = id;
        this.fechaCompra = fechaCompra;
        this.nombreCliente = nombreCliente;
        this.listaProductos = listaProductos;
    }

    public String getId(){
        return this.id;
    }
    public void setId(String ID){
        this.id = ID;
    }
    public String getFechaCompra(){
        return this.fechaCompra;
    }
    public void setFechaCompra(String fecha){
        this.fechaCompra = fecha;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public ArrayList<Producto> getListaProductos() {
        return listaProductos;
    }

    public void setListaProductos(ArrayList<Producto> listaProductos) {
        this.listaProductos = listaProductos;
    }
    
}
