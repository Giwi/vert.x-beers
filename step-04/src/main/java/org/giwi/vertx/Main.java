package org.giwi.vertx;

import io.vertx.core.Vertx;
import io.vertx.serviceproxy.ProxyHelper;
import org.giwi.vertx.service.BeerService;
import org.giwi.vertx.service.impl.BeerServiceImpl;

/**
 * The type Main.
 */
public class Main {

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String... args) {
        Vertx vertx = Vertx.vertx();
        ProxyHelper.registerService(BeerService.class, vertx, new BeerServiceImpl(vertx), BeerService.ADDRESS);
        vertx.deployVerticle(Server.class.getName(), res -> {
            if (res.succeeded()) {
                System.out.println("Started");
            } else {
                res.cause().printStackTrace();
            }
        });
    }

}
