package org.giwi.vertx;

import io.vertx.core.Vertx;

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
        vertx.deployVerticle(Server.class.getName(), res -> {
            if (res.succeeded()) {
                System.out.println("Started");
            } else {
                res.cause().printStackTrace();
            }
        });
    }

}
