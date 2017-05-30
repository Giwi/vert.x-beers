# Step 03 - Using the `Beer` service

## Add the Vert.X dependencies

    compile "io.vertx:vertx-service-proxy:3.4.1"
    compile "io.vertx:vertx-codegen:3.4.1"

## Add the code generator

    def generateSrcPath="$buildDir/generated-src"
    def generatedSrcDir = file("$buildDir/generated-src")
    repositories {
        jcenter()
    }
    sourceSets {
        main {
            java.srcDirs += generatedSrcDir
            output.dir(builtBy: 'generateServiceProxy', generateSrcPath)
        }
    }
    task generateServiceProxy(type: JavaCompile, description: 'Generates EBServiceProxies') {
        source = sourceSets.main.java
        classpath = configurations.
                compile
        destinationDir = generatedSrcDir
        options.compilerArgs = [
                "-proc:only",
                "-processor", "io.vertx.codegen.CodeGenProcessor",
                "-AoutputDirectory=$generateSrcPath"
        ]
    }
    compileJava.dependsOn generateServiceProxy


You can add to your run configuration a pre-launch step before make : launch a gradle task called generateServiceProxy

## Let's create the beer list

We are going to take the beer list from the [polymer-beers](https://github.com/LostInBrittany/polymer-beers) project
and serve it from the Vert.X server.
To do it, we begin by creating a `Beer` class that keeps the same information than the `Beer` JSON in JavaScript side.


		package org.giwi.vertx.model;

		public class Beer {

			private String name;
			private String id;
			private String img;
			private String description;
			private double alcohol;

			/*
			 ...
			 */
		}

And then, create a `BeerService` interface :

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

        String ADDRESS = "vertx.beer.service";

        static BeerService create(Vertx vertx) {
            return new BeerServiceImpl(vertx);
        }

        static BeerService createProxy(Vertx vertx, String address) {
            return ProxyHelper.createProxy(BeerService.class, vertx, address);
        }

        void getList(Handler<AsyncResult<JsonArray>> resultHandler);
    }

This interface will lead to a code generation, please note the bus *ADDRESS*. We need two static methods :

- MyService create(Vertx vertx)
- MyService createProxy(Vertx vertx, String address)

The `getList` method has a async handler as an argument. (It's tricky, ok, but useful ;) )

It's implementation :

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

We pass an async result to our handler

Add a `org/giwi/vertx/service/package-info.java`, it's a very important thing !!! please note that the package name is
the same as the groupPackage argument.

    @ModuleGen(name = "giwi", groupPackage = "org.giwi.vertx.service")
    package org.giwi.vertx.service;

    import io.vertx.codegen.annotations.ModuleGen;

In the `Main` class add

    ProxyHelper.registerService(BeerService.class, vertx, new BeerServiceImpl(vertx), BeerService.ADDRESS);

in order to register our service.

Then modify your server with :

    BeerService beerService;

    @Override
    public void start() throws Exception {
        beerService = BeerService.createProxy(vertx, BeerService.ADDRESS);

        // ...

        router.get("/*").handler(routingContext -> {
            routingContext.response()
                    .putHeader("content-type", "application/json")
                    .setChunked(true);
            routingContext.next();
        });
        router.get("/api/beer/").handler(this::beerList);

        // ...
    }

    private void beerList(RoutingContext routingContext) {
        beerService.getList(asyncResult -> routingContext.response().end(asyncResult.result().encodePrettily()));
    }


Yep, yep, yep, it's tricky but powerful. Later, you could use Guice to inject your services in your routes.