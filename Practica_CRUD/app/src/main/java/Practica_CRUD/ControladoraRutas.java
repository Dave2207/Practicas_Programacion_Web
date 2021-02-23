package Practica_CRUD;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import io.javalin.Javalin;
import static io.javalin.apibuilder.ApiBuilder.*;


public class ControladoraRutas {
    Javalin app;
    Tienda tienda = Tienda.getInstance();

    public ControladoraRutas(Javalin app) {
        this.app = app;
        Usuario yo = new Usuario("David Vasquez", "admin", "admin");
        Producto n1 = new Producto(1, "Disco duro", 500.0);
        Producto n2 = new Producto(2, "RPi 4", 2000.0);
        Producto n3 = new Producto(3, "RAM", 4000.0);
        Tienda.getInstance().agregarProducto(n1);
        Tienda.getInstance().agregarProducto(n2);
        Tienda.getInstance().agregarProducto(n3);
        for (Usuario usuario : tienda.getListaUsuarios()) {
            System.out.println("Usuario: " + usuario.getNombre());
        }
    }

    public void aplicarRutas() {
        // app.get("/", ctx -> ctx.result("Hola Mundo en Javalin :-D"));
        app.get("/", ctx -> ctx.redirect("/productos"));

        app.routes(() -> {

            get("/productos", ctx -> {
                List<Producto> listaProductos = Tienda.getInstance().getListaProductos();
                Map<String, Object> modelo = new HashMap<>();
                modelo.put("listado", listaProductos);
                if (ctx.sessionAttribute("user") == null) {
                    modelo.put("size", 0);
                } else {
                    if (ctx.sessionAttribute("carrito") == null) {
                        carroCompra carro = new carroCompra(ctx.req.getSession().getId());
                        ctx.sessionAttribute("carrito", carro);
                        modelo.put("size", 0);
                    } else {
                        modelo.put("size", ((carroCompra) ctx.sessionAttribute("carrito")).getListaProductos().size());
                    }
                }
                ctx.render("/templates/listaProductos.html", modelo);
            });

            before("/confirmarLog", ctx -> {
                String usuario = ctx.formParam("Username");
                String pass = ctx.formParam("Password");
                if (tienda.autenticarUsuario(usuario, pass) == null) {
                    ctx.redirect("/login.html");
                }
            });

            app.post("/confirmarLog", ctx -> {
                String usuario = ctx.formParam("Username");
                String pass = ctx.formParam("Password");
                String id = ctx.req.getSession().getId();
                ctx.req.getSession().invalidate();
                ctx.sessionAttribute("user", usuario);
                ctx.redirect("/");
            });

            app.post("/registrarUsuario", ctx -> {
                String usuario = ctx.formParam("Username");
                String pass = ctx.formParam("Password");
                String nombre = ctx.formParam("Name");
                System.out.println(usuario+" "+pass+" "+nombre);
                Usuario tmp = new Usuario(usuario,nombre,pass);
                tienda.agregarUsuario(tmp);
                ctx.redirect("/login.html");
            });

            app.before("/administrarProductos", ctx -> {
                if(ctx.sessionAttribute("user") == null){
                    ctx.redirect("/login.html");
                }
            });

            app.get("/administrarProductos", ctx -> {
                if(ctx.sessionAttribute("user").equals("admin")){
                    List<Producto> lista = tienda.getListaProductos();
                    Map<String, Object> modelo = new HashMap<>();
                    modelo.put("listado", lista);
                    modelo.put("size", ((carroCompra) ctx.sessionAttribute("carrito")).getListaProductos().size());
                    ctx.render("/administrarProducto.html", modelo);
                } else {
                    ctx.redirect("/error.html");
                }
            });

            app.post("/nuevoProd", ctx -> {
                int id = ctx.formParam("id", Integer.class).get();
                String nombre = ctx.formParam("nombre");
                double precio = ctx.formParam("precio", Double.class).get();
                Producto prod = new Producto(id, nombre, precio);
                tienda.agregarProducto(prod);
                ctx.redirect("/administrarProductos");
            });

            app.get("/eliminarProd/:id", ctx ->{
                int id = ctx.pathParam("id",Integer.class).get();
                Producto p = tienda.findProductoById(id);
                tienda.eliminarProducto(p);
                ctx.redirect("/administrarProductos");
            });

            app.get("/editarProd/:id", ctx ->{
                int id = ctx.pathParam("id",Integer.class).get();
                Map<String, Object> modelo = new HashMap<>();
    
                Producto p = tienda.findProductoById(id);
    
                modelo.put("id",id);
                modelo.put("nombre",p.getNombre());
                modelo.put("precio",p.getPrecio());
    
                ctx.render("/templates/editarProducto.html",modelo); //archivo a crear
            });

            app.post("/editarProd/:id", ctx -> {
                int id = ctx.pathParam("id",Integer.class).get();
                String nombre = ctx.formParam("nombre", String.class).get();
                double precio = ctx.formParam("precio", Double.class).get();
                tienda.actualizarProducto(id,nombre,precio);
                ctx.redirect("/administrarProductos");
            });

            app.before("/anadirAlCarrito", ctx -> {
                if(ctx.sessionAttribute("carrito") == null){
                    ctx.redirect("login.html");
                }
            });

        });
    }

}
