Spring MVC Plugin.
========

spring-webmvc-workflow plugin allows using Neuro4j Workflow framework together with Spring MVC in web application.

Here is example how to move your business code to workflow but use url mapping, dependency injection and security from Spring MVC.

Spring MVC Controller:	
![workflow-example-higlevel](https://raw.github.com/neuro4j/workflow/master/doc/images/spring-webmvc-workflow/controller.png "Spring MVC Controller:")	


Flow with business logic: org.springframework.samples.mvc.views.ViewFlow-Start:	
![workflow-example-higlevel](https://raw.github.com/neuro4j/workflow/master/doc/images/spring-webmvc-workflow/flow.png "Spring MVC Controller:")

Reusable java code: org.springframework.samples.mvc.views.BlockWithService.java:	
![workflow-example-higlevel](https://raw.github.com/neuro4j/workflow/master/doc/images/spring-webmvc-workflow/CustomBlock.png "Spring MVC Controller:")
