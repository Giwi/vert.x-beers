# vert.x-beers


## A server-side companion to the [polymer-beers](https://github.com/LostInBrittany/polymer-beers) project

The [polymer-beers](https://github.com/LostInBrittany/polymer-beers) project is a small Polymer tutorial that can be 
used on its own. But IMHO it is a pity to do only the client-side and mocking the server API with plain files. So here 
we have a companion project where we are going to do the server-side of 
[polymer-beers](https://github.com/LostInBrittany/polymer-beers) using [Vert.X](https://vertx.io/) for creating web 
applications in Java 8 with minimal effort.

## What are the objectives of this tutorial

Follow this tutorial to learn to build APIs in Java quickly an easily, without all the pain of the classic JEE stack. 
You will use the [Vert.X](https://vertx.io/) framework, with some drops of NoSQL databases 
(with [MongoDB](http://mongodb.com), work in progress).

## What do I need to use this tutorial

You will need a web-browser and your favorite Java IDE (I can suggest [IntelliJ IDEA](https://www.jetbrains.com/idea/)  
or [Eclipse](http://eclipse.org), but any other will do it). You will also need the [Gradle](http://gradle.org) 
build tool.

## Setup 

Edit or add ~/.gitconfig and add those lines according to your proxy's user/password : 

    [user]
    	name = Your name here
    	email = your.name@enib.fr
    [color]
    	diff = auto
    	status = auto
    	branch = auto
    [core]
    	autocrlf = input
        [http]
    	proxy = http://login:passwd@proxy.enib.fr:3128
    [https]
    	proxy = http://login:passwd@proxy.enib.fr:3128
        [credential]
    	helper = cache
    [url "http://"]
        insteadOf = git://


## How is the tutorial organized ##

As many computers used for the course haven't Git, we have structurated the project to allow a Git-less use. The `src` 
directory is the sources directory of the project, the working version of the code. The tutorial is divided in steps, 
each one in its own directory:

1. [Empty project](./step-01/)
1. [Installing Vert.X](./step-02/)
1. [Using the `Beer` service](./step-03/)
1. [Serving static resources](./step-04/)
1. [Using a Mongo DB](./step-05/)

In each step directory you have a README file that explain the objective of the step, that you will do in the working 
directory `app`. If you have problems or if you get lost, you also have the solution of each step in the step 
directories.