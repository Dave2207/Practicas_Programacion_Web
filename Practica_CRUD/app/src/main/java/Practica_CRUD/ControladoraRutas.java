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
                if (ctx.sessionAttribute("usuario") == null) {
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

            before("/admin", ctx -> {
                String usuario = ctx.formParam("Username");
                String pass = ctx.formParam("Password");
                if (tienda.autenticarUsuario(usuario, pass) == false) {
                    ctx.redirect("/login.html");
                }
            });

            app.post("/admin", ctx -> {
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

            
        });
    }

}
