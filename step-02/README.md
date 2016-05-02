# Step 02 - Installing Vert.X

We add Vert.X as a dependency for Gradle. To do it we need to define in the `build.gradle` 
file a *respository* (a source storing location when we will look for dependencies) and the 
dependency information for Vert.X :

	apply plugin: 'idea'
    apply plugin: 'java'
    apply plugin: 'com.github.johnrengelman.shadow'
    version = '0.0.1'
    
    buildscript {
        repositories { jcenter() }
        dependencies {
            classpath 'com.github.jengelman.gradle.plugins:shadow:1.2.2'
        }
    }
    
    if (!JavaVersion.current().java8Compatible) {
        throw new IllegalStateException('''A Haiku:
                                          |  This needs Java 8,
                                          |  You are using something else,
                                          |  Refresh. Try again.'''.stripMargin())
    }
    
    repositories {
        mavenCentral()
        maven {
            url = 'http://oss.sonatype.org/content/repositories/snapshots/'
        }
    }
    
    dependencies {
        compile 'io.vertx:vertx-core:3.2.1'
        compile 'io.vertx:vertx-web:3.2.1'
    }
    
    shadowJar {
        classifier = 'fat'
        manifest {
            attributes 'Main-Class': 'Main'
        }
        mergeServiceFiles {
            include 'META-INF/services/io.org.giwi.vertx.core.spi.VerticleFactory'
        }
        doLast {
            copy {
                from "$buildDir/libs/"
                into "application/"
            }
        }
    }


Then we can create a new class called `Main` and add the following code to it:

	package org.giwi.vertx;
  
    import io.vertx.core.Vertx;
    
    public class Main {
    
        public static void main(String... args) {
            Vertx vertx = Vertx.vertx();
            vertx.deployVerticle(Server.class.getName(), res -> {
                if (res.succeeded()) {
                    System.out.println("Started");
                } else {
                    System.out.println("failed");
                }
            });
        }
    
    }
	

* Imports the required classes from the Vert.X library.
* Creates a new class called `Main` and defines a main method.
* Instantiate Vert.X and launch a `Server` Verticle	

This class will be our main entrypoint in order to run Vert.X. Then, create a second class called `Server` which contains
our HTTP Server.


    package org.giwi.vertx;
    
    import io.vertx.core.AbstractVerticle;
    import io.vertx.core.http.HttpServerResponse;
    
    public class Server  extends AbstractVerticle {
        @Override
        public void start() throws Exception {
            vertx.createHttpServer().requestHandler(request -> {
                // This handler gets called for each request that arrives on the server
                HttpServerResponse response = request.response();
                response.putHeader("content-type", "text/plain");
                // Write to the response and end it
                response.end("Hello World!");
            }).listen(9090);
        }
    }

This code define an HTTP server tht handle any request and responds "Hello World!" as a plain text.
 
To see the application in action, run the main program using your IDE. The application will start
at http://0.0.0.0:9090. When you open this link [http://localhost:9090](http://localhost:9090) in your web browser, you will see *“Hello World!”*.

Take advantage of Java 8 lambda expressions to make your code more concise and clean.

Well it's not very sexy to use such an handler, let's use the powerful Vert.X router : 
 
In the `Server` class : 

    package org.giwi.vertx;
    
    import io.vertx.core.AbstractVerticle;
    import io.vertx.ext.web.Router;
    import io.vertx.ext.web.RoutingContext;
    import io.vertx.ext.web.handler.BodyHandler;
    
    public class Server  extends AbstractVerticle {
        @Override
        public void start() throws Exception {
            Router router = Router.router(vertx);
            router.route().handler(BodyHandler.create());
            router.get("/api/hello").handler(this::handleHello);
            vertx.createHttpServer().requestHandler(router::accept).listen(9090);
        }
    
        private void handleHello(RoutingContext routingContext) {
            routingContext.response().end("Hello World!");
        }
    }

When you open this link [http://localhost:9090/api/hello](http://localhost:9090/api/hello) in your web browser, you will see *“Hello World!”*.

In this bunch of code, you've seen the implementation of the Vert.X router and the handling of a GET HTTP request.

So, let's handle path parameters : 

    public void start() throws Exception {
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());
        router.get("/api/hello/:name").handler(this::handleHello);
        vertx.createHttpServer().requestHandler(router::accept).listen(9090);
    }

    private void handleHello(RoutingContext routingContext) {
        String name = routingContext.request().getParam("name");
        routingContext.response().end("Hello " + name);
    }

When you open this link [http://localhost:9090/api/hello/kitty](http://localhost:9090/api/hello/kitty) in your web browser, you will see *“Hello kitty”*.

Well, more fun, let's cascade a call : 

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

Some explanations : 

- `router.get("/*")` handles any GET request and set chunked to true (our response will be sent to the browser 
in multiple packets)
- `routingContext.next();` pass the request to the next eligible handler

OK? No question? 