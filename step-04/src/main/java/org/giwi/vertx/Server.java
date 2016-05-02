package org.giwi.vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;
import org.giwi.vertx.service.BeerService;

/**
 * The type Server.
 */
public class Server extends AbstractVerticle {

    BeerService beerService;

    @Override
    public void start() throws Exception {
        beerService = BeerService.createProxy(vertx, BeerService.ADDRESS);
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());
        router.get("/api/*").handler(routingContext -> {
            routingContext.response()
                    .putHeader("content-type", "application/json")
                    .setChunked(true);
            routingContext.next();
        });
        router.get("/api/beer/").handler(this::beerList);
        router.route("/*").handler(StaticHandler.create());
        vertx.createHttpServer().requestHandler(router::accept).listen(9090);
    }

    private void beerList(RoutingContext routingContext) {
        beerService.getList(asyncResult -> routingContext.response().end(asyncResult.result().encodePrettily()));
    }
}
