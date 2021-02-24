package Practica_CRUD;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
        tienda.agregarUsuario(yo);
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

            app.post("/confirmarLog", ctx -> {
                String usuario = ctx.formParam("Username");
                String pass = ctx.formParam("Password");
                String id = ctx.req.getSession().getId();
                ctx.req.getSession().invalidate();
                ctx.sessionAttribute("user", usuario);
                if (tienda.autenticarUsuario(usuario, pass) == null) {
                    ctx.redirect("/login.html");
                } else {
                    ctx.redirect("/");
                }
            });

            app.post("/registrarUsuario", ctx -> {
                String nombre = ctx.formParam("Username");
                String pass = ctx.formParam("Password");
                String usuario = ctx.formParam("Name");
                System.out.println(usuario + " " + pass + " " + nombre);
                Usuario tmp = new Usuario(usuario, nombre, pass);
                tienda.agregarUsuario(tmp);
                ctx.redirect("/login.html");
            });

            app.before("/administrarProductos", ctx -> {
                if (ctx.sessionAttribute("user") == null) {
                    ctx.redirect("/login.html");
                }
            });

            app.get("/administrarProductos", ctx -> {
                if (ctx.sessionAttribute("user").equals("admin")) {
                    List<Producto> lista = tienda.getListaProductos();
                    Map<String, Object> modelo = new HashMap<>();
                    modelo.put("listado", lista);
                    modelo.put("size", ((carroCompra) ctx.sessionAttribute("carrito")).getListaProductos().size());
                    ctx.render("/templates/administrarProducto.html", modelo);
                } else {
                    ctx.redirect("/error.html");
                }
            });

            app.get("/nuevoProd", ctx -> {
                ctx.render("/templates/nuevoProducto.html");
            });

            app.post("/nuevoProd", ctx -> {
                int id = ctx.formParam("id", Integer.class).get();
                String nombre = ctx.formParam("nombre");
                double precio = ctx.formParam("precio", Double.class).get();
                Producto prod = new Producto(id, nombre, precio);
                tienda.agregarProducto(prod);
                ctx.redirect("/administrarProductos");
            });

            app.get("/eliminarProd/:id", ctx -> {
                int id = ctx.pathParam("id", Integer.class).get();
                Producto p = tienda.findProductoById(id);
                tienda.eliminarProducto(p);
                ctx.redirect("/administrarProductos");
            });

            app.get("/editarProd/:id", ctx -> {
                int id = ctx.pathParam("id", Integer.class).get();
                Map<String, Object> modelo = new HashMap<>();

                Producto p = tienda.findProductoById(id);

                modelo.put("id", id);
                modelo.put("nombre", p.getNombre());
                modelo.put("precio", p.getPrecio());

                ctx.render("/templates/editarProducto.html", modelo); // archivo a crear
            });

            app.post("/editarProd/:id", ctx -> {
                int id = ctx.pathParam("id", Integer.class).get();
                String nombre = ctx.formParam("nombre", String.class).get();
                double precio = ctx.formParam("precio", Double.class).get();
                tienda.actualizarProducto(id, nombre, precio);
                ctx.redirect("/administrarProductos");
            });

            app.post("/anadirAlCarrito", ctx -> {
                int id = ctx.formParam("id", Integer.class).get();
                int cant = ctx.formParam("cantidad", Integer.class).get();
                Producto p = tienda.findProductoById(id);
                carroCompra carrito = ctx.sessionAttribute("carrito");
                for (int i = 0; i < cant; i++) {
                    carrito.agregarProducto(p);                    
                }
                if(cant > 1){
                    System.out.println("Se agregaron " + cant + " productos al carrito.");
                } else {
                    System.out.println("Se agregó 1 producto al carrito");
                }
                ctx.redirect("/");
            });

            app.get("/carrito", ctx -> {
                carroCompra carro = ctx.sessionAttribute("carrito");
                Map<String, Object> modelo = new HashMap<>();
                modelo.put("lista", carro.getListaProductos());
                modelo.put("size", ((carroCompra)ctx.sessionAttribute("carrito")).getListaProductos().size());
                ctx.render("/templates/miCarrito.html", modelo);
            });

            app.get("/eliminarDelCarrito/:id", ctx -> {
                int id = ctx.pathParam("id", Integer.class).get();
                carroCompra carrito = ctx.sessionAttribute("carrito");
                carrito.eliminarProducto(tienda.findProductoById(id));

                ctx.redirect("/carrito");
            });

            app.before("/procesar", ctx ->{
                carroCompra carrito = ctx.sessionAttribute("carrito");
                if(carrito.getListaProductos().size() == 0){
                    ctx.redirect("/carrito");
                }
            });

            app.get("/compraRealizada", ctx -> {
                ctx.render("/templates/compraRealizada.html");
            });

            app.get("/procesar", ctx -> {
                carroCompra carrito = ctx.sessionAttribute("carrito");
                SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                Date fecha = new Date();

                ArrayList<Producto> productos = new ArrayList<>();
                for (Producto p : carrito.getListaProductos()) {
                    productos.add(p);
                }

                ventasProductos venta = new ventasProductos(carrito.getId(), formato.format(fecha), ctx.sessionAttribute("user"), productos);
                tienda.agregarVenta(venta);
                carrito.getListaProductos().clear();
                ctx.redirect("/compraRealizada");
            });

            app.before("/ventasRealizadas", ctx -> {
                if(ctx.sessionAttribute("user") == null){
                    ctx.redirect("/login.html");
                }
            });

            app.get("/ventasRealizadas", ctx -> {
                ArrayList<ventasProductos> lista = tienda.getListaVentas();
                Map<String, Object> modelo = new HashMap<>();
                
                modelo.put("lista", lista);
                modelo.put("size", ((carroCompra)ctx.sessionAttribute("carrito")).getListaProductos().size());

                ctx.render("/templates/ventas.html", modelo);
            });
        });
    }

}
