package Practica_CRUD;

import java.util.ArrayList;
import java.util.Date;

public class ventasProductos {
    private int id;
    private Date fechaCompra;
    private String nombreCliente;
    private ArrayList<Producto> listaProductos;

    public ventasProductos(int id, Date fechaCompra, String nombreCliente, ArrayList<Producto> listaProductos) {
        this.id = id;
        this.fechaCompra = fechaCompra;
        this.nombreCliente = nombreCliente;
        this.listaProductos = listaProductos;
    }

    public int getId(){
        return this.id;
    }
    public void setId(int ID){
        this.id = ID;
    }
    public Date getFechaCompra(){
        return this.fechaCompra;
    }
    public void setFechaCompra(Date fecha){
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
