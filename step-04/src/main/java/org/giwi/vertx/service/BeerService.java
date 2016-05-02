package org.giwi.vertx.service;

import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.serviceproxy.ProxyHelper;
import org.giwi.vertx.service.impl.BeerServiceImpl;

@ProxyGen
@VertxGen
public interface BeerService {

    String ADDRESS = "beer.service";

    static BeerService create(Vertx vertx) {
        return new BeerServiceImpl(vertx);
    }

    static BeerService createProxy(Vertx vertx, String address) {
        return ProxyHelper.createProxy(BeerService.class, vertx, address);
    }

    void getList(Handler<AsyncResult<JsonArray>> resultHandler);
}
