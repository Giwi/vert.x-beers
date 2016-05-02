# Step 04 - Serving static resources

## We want to use Vert.X to serve the beer images 

Vert.X can also serve the static resources (images, HTML, CSS, JS...) of your application. 
To do it, you can assign a folder in the classpath serving static files in the `resources/webroot`
 method. Note that the public directory name is not included in the URL. A file `/webroot/css/style.css`
  is made available as `http://{host}:{port}/css/style.css`
		
In your project you should put the `webroot` directory inside `resources`.		

So now we can put our beer images inside `resources/webroot/img/` and serve them with URLs 
like `{host}:{port}/img/AffligemTripel.jpg`.		 

Modify the `Server` class : 

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
        // This is the good part !!
        router.route("/*").handler(StaticHandler.create());
        
        vertx.createHttpServer().requestHandler(router::accept).listen(9090);
    }
    
Beware of the declaration order !!

## Now we can make it serve also our HTML, CSS & JS, can't we? 

Yes you can! Take a stable version of the [polymer-beers](https://github.com/LostInBrittany/polymer-beers)
project (step 10 for example) and put it inside the static directory. Test it to see if it works... and 
it doesn't!

First thing to change, the beer list location. In the original 
[polymer-beers](https://github.com/LostInBrittany/polymer-beers) project we looked for a `beers/beers.json` 
file, now the have the nice `beer` API endpoint. So you need to change the `beer-list.html` component 
to ask for the right endpoint.

If you do it, you will see the [polymer-beers](https://github.com/LostInBrittany/polymer-beers) running 
as a static resource on Vert.X.

## The rest

Now, using the same model, you can implement the others rest endpoints : 

- get("/api/beer/:id") : the detail
- put("/api/beer") : add a beer
- post("/api/beer/:id") : modify a beer