package Practica_CRUD.services;

import Practica_CRUD.encapsulations.Usuario;

public class UsuarioServices extends DBManage<Usuario>{

    private static UsuarioServices inst = null;

    private UsuarioServices(){
        super(Usuario.class);
    }

    public static UsuarioServices getInstance(){
        if(inst == null){
            inst = new UsuarioServices();
        }
        return inst;
    }

    public Usuario verifyUser(String user, String password){

        Usuario aux_user = find(user);
        if(aux_user == null){
            return null;
        }else if(aux_user.getPassword().equals(password)){
            return aux_user;
        }else{
            return null;
        }

    }
}
