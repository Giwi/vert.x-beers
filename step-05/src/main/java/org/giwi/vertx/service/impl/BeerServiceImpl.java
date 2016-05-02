package org.giwi.vertx.service.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import org.giwi.vertx.service.BeerService;

import java.util.List;

/**
 * The type Beer service.
 */
public class BeerServiceImpl implements BeerService {
    private MongoClient client;
    private static final String COLLECTION = "Beers";

    /**
     * Instantiates a new Beer service.
     *
     * @param vertx the vertx
     */
    public BeerServiceImpl(Vertx vertx) {
        super();
        JsonObject config = new JsonObject()
                .put("host", "127.0.0.1")
                .put("port", 27017)
                .put("db_name", "vertx-beer");
        client = MongoClient.createShared(vertx, config);
    }

    public void getList( Handler<AsyncResult<List<JsonObject>>> resultHandler) {
        client.find(COLLECTION, new JsonObject(), resultHandler);
    }
}
