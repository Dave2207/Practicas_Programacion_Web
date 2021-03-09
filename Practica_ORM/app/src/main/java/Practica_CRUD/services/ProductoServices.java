package Practica_CRUD.services;

import Practica_CRUD.encapsulations.Producto;

public class ProductoServices extends DBManage<Producto>{
    
    private static ProductoServices inst = null;

    private ProductoServices(){
        super(Producto.class);
    }

    public static ProductoServices getInstace(){
        if(inst == null){
            inst = new ProductoServices();
        }
        return inst;
    }
}
