/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package Practica_CRUD;
import io.javalin.Javalin;
import io.javalin.plugin.rendering.JavalinRenderer;
import io.javalin.plugin.rendering.template.JavalinThymeleaf;
import static io.javalin.apibuilder.ApiBuilder.*;

import java.sql.SQLException;

public class App {
    public String getGreeting() {
        return "Hello World!";
    }

    public static void main(String[] args) throws SQLException {
        System.out.println(new App().getGreeting());

        Javalin app = Javalin.create(config -> {
            config.addStaticFiles("/templates");
        }).start(7000);
        
        BaseDatos.startDB();
        BaseDatos.crearTablas();
        

        JavalinRenderer.register(JavalinThymeleaf.INSTANCE, ".html");
        new ControladoraRutas(app).aplicarRutas();
    }
}
