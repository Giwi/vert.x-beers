package org.giwi.vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

/**
 * The type Server.
 */
public class Server extends AbstractVerticle {
    @Override
    public void start() throws Exception {
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());
        router.get("/*").handler(routingContext -> {
            routingContext.response().setChunked(true);
            routingContext.next();
        });
        router.get("/api/hello/*").handler(this::handleHello);
        router.get("/api/hello/:name").handler(this::handleName);
        vertx.createHttpServer().requestHandler(router::accept).listen(9090);
    }

    private void handleHello(RoutingContext routingContext) {
        routingContext.response().write("Hello ");
        routingContext.next();
    }

    private void handleName(RoutingContext routingContext) {
        String name = routingContext.request().getParam("name");
        routingContext.response().end(name);
    }
}
