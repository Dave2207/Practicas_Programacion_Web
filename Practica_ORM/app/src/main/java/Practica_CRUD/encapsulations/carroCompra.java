package Practica_CRUD.encapsulations;

import java.util.ArrayList;

public class carroCompra {
    private String id;
    private ArrayList<DetalleProducto> listaProductos;

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

    public ArrayList<DetalleProducto> getListaProductos() {
        return listaProductos;
    }

    public void setListaProductos(ArrayList<DetalleProducto> listaProductos) {
        this.listaProductos = listaProductos;
    }

    public void agregarProducto(DetalleProducto prod){
        listaProductos.add(prod);
    }
    public void eliminarProducto(DetalleProducto prod){
        listaProductos.remove(prod);
    }

    public int cartSize(){
        int size = 0;
        for (DetalleProducto p : listaProductos) {
            size += p.getCantidad();         
        }
        return size;
    }

    public DetalleProducto buscarProducto(int id){
        DetalleProducto detP = null;
        for (DetalleProducto dp : listaProductos) {
            if(dp.getProducto().getId() == id){
                detP = dp;
                break;
            }
        }
        return detP;
    }

}
