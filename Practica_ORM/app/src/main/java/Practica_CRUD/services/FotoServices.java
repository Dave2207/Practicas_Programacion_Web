package Practica_CRUD.services;

import Practica_CRUD.encapsulations.Foto;

public class FotoServices extends DBManage<Foto>{
    
    private static FotoServices inst = null;

    private FotoServices(){
        super(Foto.class);
    }

    public static FotoServices getInstance(){
        if(inst == null){
            inst = new FotoServices();
        }
        return inst;
    }
}
