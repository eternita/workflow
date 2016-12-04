Netflix Governator Plugin.
========

netflix-governator-adapter  allows developer to initialize custom blocks using Netflix Governator library.

Netflix Governator is a library of extensions and utilities that extend Google Guice to provide:
 
 * Classpath scanning and automatic binding
 * Lifecycle management
 * Configuration to field mapping
 * Field validation
 * Parallelized object warmup
 * Lazy singleton support
 * Generic binding annotations
 
 Example:
 ```
Injector injector = LifecycleInjector.builder().withModuleClass(AppInjector.class)
                                     .usingBasePackages("org.neuro4j").build().createInjector();
   
GovernatorCustomBlockInitStrategy	initStrategy = new GovernatorCustomBlockInitStrategy(injector);
 
WorkflowEngine engine = new WorkflowEngine(new ConfigBuilder().withCustomBlockInitStrategy(initStrategy));

ExecutionResult result = engine.execute("org.neuro4j.workflow.governator.flows.Flow-Start");

 ```
