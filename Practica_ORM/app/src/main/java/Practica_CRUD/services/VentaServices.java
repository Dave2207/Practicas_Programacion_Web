package Practica_CRUD.services;

import Practica_CRUD.encapsulations.ventasProductos;

public class VentaServices extends DBManage<ventasProductos>{
    
    private static VentaServices inst = null;

    private VentaServices(){
        super(ventasProductos.class);
    }

    public static VentaServices getInstance(){
        if(inst == null){
            inst = new VentaServices();
        }
        return inst;
    }
}
