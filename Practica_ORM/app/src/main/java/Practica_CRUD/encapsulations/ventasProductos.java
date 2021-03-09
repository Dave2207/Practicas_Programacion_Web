package Practica_CRUD.encapsulations;

import java.io.Serializable;
import java.util.ArrayList;
import javax.persistence.*;

@Entity
public class ventasProductos implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private String fechaCompra;
    private String nombreCliente;
    @OneToMany (fetch = FetchType.EAGER)
    private ArrayList<DetalleProducto> listaProductos;
    private double total;

    public ventasProductos(){

    }

    public ventasProductos(String fechaCompra, String nombreCliente, ArrayList<DetalleProducto> listaProductos){
        this.fechaCompra = fechaCompra;
        this.nombreCliente = nombreCliente;
        this.listaProductos = listaProductos;
        this.total = totalVenta();
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

    public ArrayList<DetalleProducto> getListaProductos() {
        return listaProductos;
    }

    public void setListaProductos(ArrayList<DetalleProducto> listaProductos) {
        this.listaProductos = listaProductos;
    }
    
    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double totalVenta(){
        double total = 0;
        double aux;
        for (DetalleProducto prod: listaProductos) {
            aux = prod.getCantidad()*prod.getProducto().getPrecio();
            total += aux;
        }
        return total;
    }
    
}
