[![Build Status](https://secure.travis-ci.org/neuro4j/workflow.png?branch=master)](https://travis-ci.org/neuro4j/workflow)
[![Coverage Status](https://coveralls.io/repos/github/neuro4j/workflow/badge.svg)](https://coveralls.io/github/neuro4j/workflow)

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
  

## First Flow
----------

This is HelloWorld example how to use Neuro4j Flows.

Flow file org\neuro4j\example\HelloWorld.n4j:

![workflow-example-higlevel](https://raw.github.com/neuro4j/workflow/master/doc/images/HelloWorld.png "HelloWorld workflow")


HelloWorld.java

![workflow-example-higlevel](https://raw.github.com/neuro4j/workflow/master/doc/images/HelloWorldCustomBlock.png "HelloWorld workflow")

Following code execute flow:

![workflow-example-higlevel](https://raw.github.com/neuro4j/workflow/master/doc/images/MainClass.png "HelloWorld workflow")


Online documentation how to create first application based on flow available at (http://neuro4j.org/articles/tutorial_hello_world)

### Loading workflow
Workflow are loaded using the ```WorkflowLoader``` class. neuro4j-core provides three implementations of a ```WorkflowLoader```:
 * ClassPathWorkflowLoader
 * FileWorkflowLoader
 * RemoteWorkflowLoader

WorkflowLoader uses hierarchical delegation - if loader is not able to load workflow it delegates task to next loader
```
		WorkflowEngine engine = new WorkflowEngine(new ConfigBuilder()
			                                          	.withLoader(new RemoteWorkflowLoader(converter, new ClasspathWorkflowLoader(converter))));
```
or
```
		File baseDir ...
		FileWorkflowLoader loader = new FileWorkflowLoader(converter, new ClasspathWorkflowLoader(converter), baseDir);
  		WorkflowEngine engine = new WorkflowEngine(new ConfigBuilder()
			                                          	.withLoader(loader));

```
#### ClassPathWorkflowLoader
Loads workflows from folders/jars in classpath

#### FileWorkflowLoader
Loads workflows from some external folder. Can be used to overwrite workflow from classpath

#### RemoteWorkflowLoader
Loads remote workflow over http/https. 

### Workflow cache
WorkflowProcessor keeps all loaded and converted workflows in cache. By default it uses ConcurrentMapWorkflowCache but during development
WorkflowEngine can be configured to use EmptyWorkflowCache.
```
		WorkflowEngine engine = new WorkflowEngine(
			             	                  new ConfigBuilder()
                                   .withWorkflowCache(EmptyWorkflowCache.INSTANCE));
    
  engine.execute("org.neuro4j.workflow.tutorial.HelloFlow-Start", parameters);
```

### Aliases
WorkflowEngine can be configured to use aliases for workflows.
```
		Map<String, String> map = new HashMap<>();
		map.put("myflow", "org.mydomain.FlowForFileWorkflowLoader-StartNode1");

		ConfigBuilder builder = new ConfigBuilder().withAliases(map);
  
		WorkflowEngine engine = new WorkflowEngine(builder);
		ExecutionResult result = engine.execute("myflow");
```
Flow ```org.mydomain.FlowForFileWorkflowLoader-StartNode1``` will be loaded and processed.


### ActionHandler
Developers can define ActionHandler to execute code before/after CustomBlock
```
		ActionHandler handler = new ActionHandler() {

			@Override
			public void preExecute(NodeInfo nodeInfo, FlowContext context, ActionBlock actionBlock) {
			}

			@Override
			public void postExecute(NodeInfo nodeInfo, FlowContext context, ActionBlock actionBlock) {
			}
		};

		Map<Class<? extends ActionBlock>, ActionHandler> map = new HashMap<>();
		
		map.put(SystemOutBlock.class, handler);
		
		WorkflowEngine engine = new WorkflowEngine(new ConfigBuilder().withActionRegistry(new ActionHandlersRegistry(map)));
		
```

```WorkflowProcessor``` will call handler before/after each call ```SystemOutBlock.execute(....)```

### CustomBlock cache
Developers can define cache strategy for each custom block using @CachedNode annotation
 * SINGLETON
 * NODE
 * NONE

#### SINGLETON 
Just one instance of block will be created. ex. SystemOutBlock

#### NONE
WorkflowLoader will create new instance of block for each call.

#### NODE

Debuging flow
----------

Neuro4j Studio provides  plugin which allows to debug flow in visual editor.

![workflow-example-higlevel](https://raw.github.com/neuro4j/workflow/master/doc/images/debug.png "Debuging workflow")

More information about debug plugin avaliable at http://neuro4j.org/docs/wf/flowdebugtool

