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
    }

    public void aplicarRutas(){
        app.routes(() -> {
            path("/", () -> {

                get("/", ctx -> {
                    List<Producto> listaProductos = Tienda.getInstance().getListaProductos();
                    Map<String, Object> modelo = new HashMap<>();
                    modelo.put("listado", Tienda.getInstance().getListaProductos());
                    ctx.render("/resources/templates/listaProductos.html", modelo);
                });

            });
        });
    }

}
