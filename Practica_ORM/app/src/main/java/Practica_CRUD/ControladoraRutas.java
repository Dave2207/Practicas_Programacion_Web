package Practica_CRUD;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.checkerframework.checker.units.qual.mol;

import javax.persistence.*;

import io.javalin.Javalin;
import static io.javalin.apibuilder.ApiBuilder.*;
import Practica_CRUD.encapsulations.ventasProductos;
import Practica_CRUD.services.ComentarioServices;
import Practica_CRUD.services.DetalleProductoServices;
import Practica_CRUD.services.FotoServices;
import Practica_CRUD.services.ProductoServices;
import Practica_CRUD.services.UsuarioServices;
import Practica_CRUD.services.VentaServices;
import Practica_CRUD.encapsulations.Comentario;
import Practica_CRUD.encapsulations.DetalleProducto;
import Practica_CRUD.encapsulations.Foto;
import Practica_CRUD.encapsulations.Producto;
import Practica_CRUD.encapsulations.Usuario;
import Practica_CRUD.encapsulations.carroCompra;

public class ControladoraRutas {
    Javalin app;
    //Tienda tienda = Tienda.getInstance();

    public ControladoraRutas(Javalin app) {
        this.app = app;
        // Usuario yo = new Usuario("David Vasquez", "admin", "admin");
        // tienda.agregarUsuario(yo);
        // Producto n1 = new Producto(1, "Disco duro", 500.0);
        // Producto n2 = new Producto(2, "RPi 4", 2000.0);
        // Producto n3 = new Producto(3, "RAM", 4000.0);
        // Tienda.getInstance().agregarProducto(n1);
        // Tienda.getInstance().agregarProducto(n2);
        // Tienda.getInstance().agregarProducto(n3);
        // for (Usuario usuario : tienda.getListaUsuarios()) {
        //     System.out.println("Usuario: " + usuario.getNombre());
        // }
    }

    public void aplicarRutas() {
        // app.get("/", ctx -> ctx.result("Hola Mundo en Javalin :-D"));
        // app.before("/", ctx -> {
        //     if(ctx.sessionAttribute("user") == null){
        //         ctx.redirect("/login.html");
        //     }
        // });
        app.get("/", ctx -> ctx.redirect("/productos/1"));

        app.routes(() -> {
            //Pagina principal, lista de productos
            get("/productos/:page", ctx -> {
                EntityManager em = ProductoServices.getInstace().getEntityManager();
                String page = ctx.pathParam("page", String.class).get();
                //Tomo productos de base de datos
                int pageSize = 10;
                String countQ = "Select count (p.id) from Producto p";
                Query countQuery = em.createQuery(countQ);
                Long countResults = (Long) countQuery.getSingleResult();

                int pageCant = (int) Math.ceil(countResults/pageSize);
                int lastPageNumber;
                if(page == ""){
                    lastPageNumber = 1;
                } else{
                    lastPageNumber = Integer.valueOf(page);
                }

                ctx.sessionAttribute("currentp", lastPageNumber);

                Query query = em.createQuery("from Producto");
                query.setFirstResult((lastPageNumber-1)*pageSize);
                query.setMaxResults(pageSize);
                List<Producto> lista = query.getResultList();
                //List<Producto> lista = ProductoServices.getInstace().findAll();

                Map<String, Object> modelo = new HashMap<>();
                modelo.put("pageCant", pageCant);
                modelo.put("lista", lista);
                modelo.put("user", ctx.sessionAttribute("user"));

                if(ctx.sessionAttribute("user") == null){
                    modelo.put("size", 0);
                } else {
                    if(ctx.sessionAttribute("carrito") == null){
                        carroCompra carrito = new carroCompra(ctx.req.getSession().getId());
                        ctx.sessionAttribute("carrito", carrito);
                        modelo.put("size", 0);
                    } else {
                        modelo.put("size", ((carroCompra)ctx.sessionAttribute("carrito")).cartSize());
                    }
                }

                ctx.render("/templates/listaProductos.html", modelo);
            });
            //Login y usuario
            before("/confirmarLog", ctx ->{
                String user = ctx.formParam("Username");
                String pass = ctx.formParam("Password");
                if(UsuarioServices.getInstance().verifyUser(user, pass) == null){
                    ctx.redirect("/login.html");
                }
            });

            post("/confirmarLog", ctx -> {
                String usuario = ctx.formParam("Username");
                ctx.req.getSession().invalidate();
                ctx.sessionAttribute("user", usuario);
                ctx.redirect("/");
            });

            post("/registrarUsuario", ctx -> {
                String nombre = ctx.formParam("Username");
                String pass = ctx.formParam("Password");
                String usuario = ctx.formParam("Name");
                Usuario tmp = new Usuario(usuario, nombre, pass);
                UsuarioServices.getInstance().crear(tmp);
                // tienda.agregarUsuario(tmp);
                ctx.redirect("/login.html");
            });

            before("/administrarProductos", ctx -> {
                if (ctx.sessionAttribute("user") == null) {
                    ctx.redirect("/login.html");
                }
            });

            get("/administrarProductos", ctx -> {
                if(ctx.sessionAttribute("user").equals("admin")){
                    List<Producto> lista = ProductoServices.getInstace().findAll();
                    Map<String, Object> modelo = new HashMap<>();
                    modelo.put("lista", lista);
                    modelo.put("size", ((carroCompra)ctx.sessionAttribute("carrito")).cartSize());
                    modelo.put("user", ctx.sessionAttribute("user"));
                    ctx.render("/templates/administrarProducto.html", modelo);
                } else {
                    ctx.redirect("/error.html");
                }
            });

            get("/nuevoProd", ctx -> {
                ctx.render("/templates/nuevoProducto.html");
            });
            //Rutas del CRUD de producto
            post("/nuevoProd", ctx -> {
                String nombre = ctx.formParam("nombre");
                double precio = ctx.formParam("precio", Double.class).get();
                String descripcion = ctx.formParam("descripcion");

                Producto prod = new Producto(nombre, precio, descripcion);
                ProductoServices.getInstace().crear(prod);
                ctx.uploadedFiles("foto").forEach(uploadedFile -> {
                    try {
                        byte[] bytes = uploadedFile.getContent().readAllBytes();
                        String encodedString = Base64.getEncoder().encodeToString(bytes);
                        Foto foto = new Foto(uploadedFile.getFilename(), uploadedFile.getContentType(), encodedString, prod);
                        FotoServices.getInstance().crear(foto);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    ctx.redirect("/administrarProductos");
                });
            });

            get("/eliminarProd/:id", ctx -> {
                int id = ctx.pathParam("id", Integer.class).get();
                ProductoServices.getInstace().eliminar(id);
                ctx.redirect("/administrarProductos");
            });

            get("/editarProd/:id", ctx -> {
                int id = ctx.pathParam("id", Integer.class).get();
                Map<String, Object> modelo = new HashMap<>();

                Producto p = ProductoServices.getInstace().find(id);

                modelo.put("id", id);
                modelo.put("nombre", p.getNombre());
                modelo.put("precio", p.getPrecio());
                modelo.put("descripcion", p.getDescripcion());
                modelo.put("user", ctx.sessionAttribute("user"));

                ctx.render("/templates/editarProducto.html", modelo);
            });

            post("/editarProd/:id", ctx -> {
                int id = ctx.pathParam("id", Integer.class).get();
                String nombre = ctx.formParam("nombre");
                double precio = ctx.formParam("precio", Double.class).get();
                String descripcion = ctx.formParam("descripcion");
                Producto prod = new Producto(id, nombre, precio, descripcion);
                ProductoServices.getInstace().editar(prod);
                ctx.redirect("/administrarProductos");
            });
            
            //Rutas del carrito
            // before("/anadirAlCarrito", ctx -> {
            //     if(ctx.sessionAttribute("carrito") == null){
            //         ctx.redirect("/login.html");
            //     }
            // });
            before("/carrito", ctx -> {
                if(ctx.sessionAttribute("carrito") == null){
                    ctx.redirect("/login.html");
                }
            });

            post("/anadirAlCarrito", ctx -> {
                int id = ctx.formParam("id", Integer.class).get();
                int cant = ctx.formParam("cantidad", Integer.class).get();

                carroCompra carrito = ctx.sessionAttribute("carrito");
                if(cant > 0){
                    DetalleProducto dp = carrito.buscarProducto(id);

                    if(dp == null){
                        Producto tmp = ProductoServices.getInstace().find(id);
                        dp = new DetalleProducto(tmp, cant);
                        carrito.agregarProducto(dp);
                    } else {
                        dp.setCantidad(dp.getCantidad()+cant);
                    }
                }
                ctx.redirect("/productos/"+ctx.sessionAttribute("currentp"));
            });

            get("/carrito", ctx -> {
                carroCompra carro = ctx.sessionAttribute("carrito");
                Map<String, Object> modelo = new HashMap<>();
                modelo.put("lista", carro.getListaProductos());
                modelo.put("size", ((carroCompra)ctx.sessionAttribute("carrito")).cartSize());
                modelo.put("user", ctx.sessionAttribute("user"));
                ctx.render("/templates/miCarrito.html", modelo);
            });

            post("/eliminarDelCarrito/:id", ctx -> {
                int id = ctx.pathParam("id", Integer.class).get();
                int cant = ctx.formParam("cantidad", Integer.class).get();
                carroCompra carrito = ctx.sessionAttribute("carrito");
                
                DetalleProducto dp = carrito.buscarProducto(id);
                dp.setCantidad(dp.getCantidad()-cant);

                if(dp.getCantidad() < 1){
                    carrito.getListaProductos().remove(dp);
                }

                ctx.redirect("/carrito");
            });

            before("/procesar", ctx -> {
                carroCompra carrito = ctx.sessionAttribute("carrito");
                if (carrito.cartSize() == 0) {
                    ctx.redirect("/carrito");
                }
            });

            get("/compraRealizada", ctx -> {
                ctx.render("/templates/compraRealizada.html");
            });

            get("/procesar", ctx -> {
                carroCompra carrito = ctx.sessionAttribute("carrito");
                SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                Date fecha = new Date();

                List<DetalleProducto> dps = new ArrayList<>();

                for (DetalleProducto dp : carrito.getListaProductos()) {
                    dps.add(dp);
                    DetalleProductoServices.getInstance().crear(dp);
                }
                ventasProductos venta = new ventasProductos(formato.format(fecha), ctx.sessionAttribute("user"), dps);
                VentaServices.getInstance().crear(venta);

                carrito.getListaProductos().clear();

                ctx.redirect("/compraRealizada");
            });

            before("/ventasRealizadas", ctx -> {
                if (ctx.sessionAttribute("user") == null) {
                    ctx.redirect("/login.html");
                } else {
                    if(!ctx.sessionAttribute("user").equals("admin")){
                        ctx.redirect("/error.html");
                    }
                }
            });

            get("/ventasRealizadas", ctx -> {
                List<ventasProductos> lista = VentaServices.getInstance().findAll();
                Map<String, Object> modelo = new HashMap<>();

                modelo.put("lista", lista);
                modelo.put("size", ((carroCompra) ctx.sessionAttribute("carrito")).getListaProductos().size());
                modelo.put("user", ctx.sessionAttribute("user"));

                ctx.render("/templates/ventas.html", modelo);
            });

            get("/prodInfo/:id", ctx -> {
                int id = ctx.pathParam("id", Integer.class).get();
                Map<String, Object> modelo = new HashMap<>();

                List<Comentario> lista;
                Producto prod = ProductoServices.getInstace().find(id);

                EntityManager em = ComentarioServices.getInstance().getEntityManager();
                String queryString = "select c from Comentario c where c.producto.id = :id";
                Query query = em.createQuery(queryString);
                query.setParameter("id", id);
                lista = query.getResultList();

                modelo.put("lista", lista);
                modelo.put("producto", prod);
                modelo.put("cant",lista.size());
                modelo.put("usuario",ctx.sessionAttribute("user"));

                ctx.render("/templates/vistaComentario.html", modelo);
            });

            before("/newComentario", ctx -> {
                if(ctx.sessionAttribute("user") == null){
                    ctx.redirect("/login.html");
                }
            });

            post("/newComentario", ctx -> {
            int id = ctx.formParam("id", Integer.class).get();
            Producto producto = ProductoServices.getInstace().find(id);
            Usuario usuario = UsuarioServices.getInstance().find(ctx.sessionAttribute("user"));
            String content = ctx.formParam("contenido", String.class).get();
            
            Comentario comentario = new Comentario(usuario, producto, content);
            ComentarioServices.getInstance().crear(comentario);

            ctx.redirect("/prodInfo/"+id);

            });

            post("/eliminarComentario/:id", ctx -> {
                int id = ctx.pathParam("id",Integer.class).get();
                int idProd = ctx.formParam("idProd", Integer.class).get();
                ComentarioServices.getInstance().eliminar(id);
                ctx.redirect("/prodInfo/"+idProd);
            });
        });
    }

}
