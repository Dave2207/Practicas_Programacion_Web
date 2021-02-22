/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package Practica_CRUD;
import io.javalin.Javalin;
import io.javalin.core.util.RouteOverviewPlugin;
import io.javalin.plugin.openapi.OpenApiPlugin;
import io.javalin.plugin.rendering.JavalinRenderer;
import io.javalin.plugin.rendering.template.JavalinThymeleaf;

public class App {
    public String getGreeting() {
        return "Hello World!";
    }

    public static void main(String[] args) {
        System.out.println(new App().getGreeting());

        Javalin app = Javalin.create(config -> {
            config.addStaticFiles("/templates");
        }).start(7000);

        JavalinRenderer.register(JavalinThymeleaf.INSTANCE, ".html");
        new ControladoraRutas(app).aplicarRutas();
    }
}
