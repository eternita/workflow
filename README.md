[![Build Status](https://secure.travis-ci.org/neuro4j/workflow.png?branch=master)](https://travis-ci.org/neuro4j/workflow)

Neuro4j Workflow.
========
Java workflow engine with Eclipse-based development environment.


What are the advantages of Neuro4j Workflow framework?
----------------

The advantages of Workflows are as follows:

* Neuro4j Workflow has layered architecture.
 
* It will be much easier to organize, read and support your code.
 
* Reusable business code, no need for duplication. 

* Module Development
 
* Easy to integrate with different technologies. 

* Dependency Injection.
 Developer can use Google Guice library or Spring framework to initialize code.

* Open source.
  

First Flow
----------

This is HelloWorld example how to use Neuro4j Flows.

Flow file org\neuro4j\example\HelloWorld.n4j:

![workflow-example-higlevel](https://raw.github.com/neuro4j/workflow/master/doc/images/HelloWorld.png "HelloWorld workflow")


HelloWorld.java

![workflow-example-higlevel](https://raw.github.com/neuro4j/workflow/1.3.0/doc/images/HelloWorldCustomBlock.png "HelloWorld workflow")

Following code execute flow:

![workflow-example-higlevel](https://raw.github.com/neuro4j/workflow/master/doc/images/MainClass.png "HelloWorld workflow")


Online documentation how to create first application based on flow available at (http://neuro4j.org/articles/tutorial_hello_world)


How It Works?
----------

![workflow-how-works](https://raw.githubusercontent.com/neuro4j/workflow/master/doc/images/diagram.png "How it works")



Debuging flow
----------

Neuro4j Studio provides  plugin which allows to debug flow in visual editor.

![workflow-example-higlevel](https://raw.github.com/neuro4j/workflow/master/doc/images/debug.png "Debuging workflow")

More information about debug plugin avaliable at http://neuro4j.org/docs/wf/flowdebugtool

