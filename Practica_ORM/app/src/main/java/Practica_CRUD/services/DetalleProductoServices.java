package Practica_CRUD.services;

import Practica_CRUD.encapsulations.DetalleProducto;

public class DetalleProductoServices extends DBManage<DetalleProducto>{
       
    private static DetalleProductoServices inst = null;

    private DetalleProductoServices(){
        super(DetalleProducto.class);
    }

    public static DetalleProductoServices getInstance(){
        if(inst == null){
            inst = new DetalleProductoServices();
        }
        return inst;
    }
}
