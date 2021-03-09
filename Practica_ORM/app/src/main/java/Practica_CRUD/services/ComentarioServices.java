package Practica_CRUD.services;

import Practica_CRUD.encapsulations.Comentario;

public class ComentarioServices extends DBManage<Comentario>{
       
    private static ComentarioServices inst = null;

    private ComentarioServices(){
        super(Comentario.class);
    }

    public static ComentarioServices getInstance(){
        if(inst == null){
            inst = new ComentarioServices();
        }
        return inst;
    }
}
