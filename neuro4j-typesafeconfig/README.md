TypeSaveConfig plugin.
========

neuro4j-typesafeconfig plugin allows to use typesafe library to configure workflow processor from  config file

* add file ```application.conf``` to classpath
* add new configuration
```
neuro4j{

// will not use cache - can be used for dev env
  workflowCache = emptyMap

 aliases {
     // will map myflow -> org.neuro4j.MyFlow-Start
     myflow=org.neuro4j.MyFlow-Start
     myflow2=org.neuro4j.workflow.MyFlow-StartNode2
     "/homepage.htm"=org.neuro4j.workflow.HomePage-Start
  }
  
  // pool setting for parallel execution
    threadPoolTaskConfig{
  
  	corePoolSize = 4

		maxPoolSize = 2147483647

		keepAliveSeconds = 65

		queueCapacity = 2147483647
		
		allowCoreThreadTimeOut = false
  }
  
}
```
* Create configuration with  WorkflowConfigFactory
```
ConfigBuilder config = WorkflowConfigFactory.load();
// add specific configuration
WorkflowEngine engine = new WorkflowEngine(config);

```
