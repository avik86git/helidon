
package io.helidon.examples.quickstart.se;

import io.helidon.common.LogConfig;
import io.helidon.config.Config;
import io.helidon.health.HealthSupport;
import io.helidon.health.checks.HealthChecks;
import io.helidon.media.jsonp.JsonpSupport;
//import io.helidon.media.jsonb.server.JsonBindingSupport;
import io.helidon.media.jsonb.JsonbSupport;
import io.helidon.metrics.MetricsSupport;
import io.helidon.webserver.Routing;
import io.helidon.webserver.WebServer;
import io.helidon.webserver.cors.CorsSupport;
import io.helidon.webserver.cors.CrossOriginConfig;
import org.eclipse.yasson.internal.JsonBinding;

import org.fluentd.logger.FluentLogger;

/**
 * The application main class.
 */
public final class Main {


    /**
     * Cannot be instantiated.
     */
    private Main() {
    }

    /**
     * Application main entry point.
     * @param args command line arguments.
     */
    public static void main(final String[] args) {
        //FluentLogger LOG = FluentLogger.getLogger("app", "129.213.13.244", 24224);
        System.out.println("In main before log");
        //LOG.log("kubernetes","Msg1","{source: 'find_item_api', message: 'Successful call to SaaS API - '+itemNum}");
        startServer();
    }

    /**
     * Start the server.
     * @return the created {@link WebServer} instance
     */
    static WebServer startServer() {
        // load logging configuration
        LogConfig.configureRuntime();

        // By default this will pick up application.yaml from the classpath
        Config config = Config.create();

        // Build server with JSONP support
        WebServer server = WebServer.builder(createRouting(config))
                .config(config.get("server"))
                .addMediaSupport(JsonpSupport.create())
                .build();

        // Try to start the server. If successful, print some info and arrange to
        // print a message at shutdown. If unsuccessful, print the exception.
        server.start()
                .thenAccept(ws -> {
                    System.out.println(
                            "WEB server is up! http://localhost:" + ws.port() + "/item-reservation");
                    ws.whenShutdown().thenRun(()
                            -> System.out.println("WEB server is DOWN. Good bye!"));
                })
                .exceptionally(t -> {
                    System.err.println("Startup failed: " + t.getMessage());
                    t.printStackTrace(System.err);
                    return null;
                });

        // Server threads are not daemon. No need to block. Just react.

        return server;
    }

    /**
     * Creates new {@link Routing}.
     *
     * @return routing configured with JSON support, a health check, and a service
     * @param config configuration of this server
     */
    private static Routing createRouting(Config config) {
        MetricsSupport metrics = MetricsSupport.create();
        ItemReservationService itemReservationService = new ItemReservationService(config);
        /*CorsSupport corsSupport = CorsSupport.builder()
                .addCrossOrigin(CrossOriginConfig.create())
                .build();*/
        CorsSupport corsSupport1 = CorsSupport.builder()
                .addCrossOrigin(CrossOriginConfig.builder()
                        .allowOrigins("*")
                        .allowMethods("*")
                        .allowHeaders("*")
                        .enabled(true)
                        .build()
                )
                .addCrossOrigin(CrossOriginConfig.create())
                .build();

        HealthSupport health = HealthSupport.builder()
                .addLiveness(HealthChecks.healthChecks())   // Adds a convenient set of checks
                .build();
        //new Main().callItemSearch();
        return Routing.builder()
                //.register(JsonBindingSupport.create())
                //.register(JsonpSupport.create())
                .register(health)                   // Health at "/health"
                .register(metrics)                  // Metrics at "/metrics"
                //.register("/greet", greetService)
                .register("/item-reservation",corsSupport1, itemReservationService)
                .build();
    }

    public void callItemSearch(){

    }
}
