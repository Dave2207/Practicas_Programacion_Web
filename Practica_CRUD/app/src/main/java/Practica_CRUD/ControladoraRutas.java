package Practica_CRUD;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import io.javalin.Javalin;
import static io.javalin.apibuilder.ApiBuilder.*;
import io.javalin.plugin.rendering.JavalinRenderer;
import io.javalin.plugin.rendering.template.JavalinThymeleaf;

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
    }

    public void aplicarRutas() {
        //app.get("/", ctx -> ctx.result("Hola Mundo en Javalin :-D"));
        app.get("/", ctx -> ctx.redirect("/productos"));

        app.routes(() -> {

            get("/productos", ctx -> {
                List<Producto> listaProductos = Tienda.getInstance().getListaProductos();
                Map<String, Object> modelo = new HashMap<>();
                modelo.put("listado", listaProductos);
                ctx.render("/templates/listaProductos.html", modelo);
            });
            
        });
    }

}
