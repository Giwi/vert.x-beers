# Step 05 - Mongo DB 

## We want to use Vert.X to use Mongo DB

First insert a bunch of data : 

    <path/to/mongo/bin/>mongo vertx-beer data/beers.js

Import Mongo : 

    compile 'io.vertx:vertx-mongo-client:3.2.1'
    
In the `BeerServiceImpl` 

    public class BeerServiceImpl implements BeerService {
        private MongoClient client;
        private static final String COLLECTION = "Beers";
        
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

See [http://vertx.io/docs/vertx-mongo-client/java/](http://vertx.io/docs/vertx-mongo-client/java/)

Then modify the `Server` class :
 
    private void beerList(RoutingContext routingContext) {
        beerService.getList(asyncResult -> {
            JsonArray beerList = new JsonArray();
            asyncResult.result().forEach(beerList::add);
            routingContext.response().end(beerList.encodePrettily());
        });
    }

## The rest

Now, using the same model, you can implement the others rest endpoints : 

- get("/api/beer/:id") : the detail
- put("/api/beer") : add a beer
- post("/api/beer/:id") : modify a beer