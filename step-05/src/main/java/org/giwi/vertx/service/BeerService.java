package org.giwi.vertx.service;

import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.serviceproxy.ProxyHelper;
import org.giwi.vertx.service.impl.BeerServiceImpl;

import java.util.List;

/**
 * The interface Beer service.
 */
@ProxyGen
@VertxGen
public interface BeerService {

    /**
     * The constant ADDRESS.
     */
    String ADDRESS = "beer.service";

    /**
     * Create beer service.
     *
     * @param vertx the vertx
     * @return the beer service
     */
    static BeerService create(Vertx vertx) {
        return new BeerServiceImpl(vertx);
    }

    /**
     * Create proxy beer service.
     *
     * @param vertx   the vertx
     * @param address the address
     * @return the beer service
     */
    static BeerService createProxy(Vertx vertx, String address) {
        return ProxyHelper.createProxy(BeerService.class, vertx, address);
    }

    /**
     * Gets list.
     *
     * @param resultHandler the result handler
     */
    void getList( Handler<AsyncResult<List<JsonObject>>> resultHandler);
}
