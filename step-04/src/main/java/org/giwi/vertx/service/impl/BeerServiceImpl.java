package org.giwi.vertx.service.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.giwi.vertx.model.Beer;
import org.giwi.vertx.service.BeerService;

public class BeerServiceImpl implements BeerService {
    public BeerServiceImpl(Vertx vertx) {
        super();
    }

    public void getList(Handler<AsyncResult<JsonArray>> resultHandler) {
        JsonArray list = new JsonArray();
        Beer beer = new Beer();
        beer.setAlcohol(6.8);
        beer.setDescription("Affligem Blonde, the classic clear blonde abbey ale, with a gentle roundness and 6.8% alcohol. Low on bitterness, it is eminently drinkable.");
        beer.setId("AffligemBlond");
        beer.setImg("beers/img/AffligemBlond.jpg");
        beer.setName("Affligem Blond");
        list.add(new JsonObject(Json.encode(beer)));
        // ...
        resultHandler.handle(Future.succeededFuture(list));
    }
}
