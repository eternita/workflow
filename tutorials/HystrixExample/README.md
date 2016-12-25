## Netflix Hystrix in workflow 

Let's create simple SpringBoot application to download documents but each operation will be a HystrixCommand
 
### SpringBoot configuration


SpringBoot configuration file AppConfig.java defines WorkflowEngine and 
SpringContextInitStrategy which allows to initialize component inside workflow Spring's BeanFactory

```

@Configuration
public class AppConfig {

	@Autowired
	SpringContextInitStrategy initStrategy;

	@Bean
	@Scope("singleton")
	public WorkflowEngine getWorkflowEngine() {
		return new WorkflowEngine(new ConfigBuilder().withCustomBlockInitStrategy(initStrategy));
	}

}

```
SampleApplication with @EnableHystrixDashboard annotation
```
@SpringBootApplication
@ComponentScan({"org.neuro4j"})
@EnableHystrixDashboard
public class SampleApplication  {

	public static void main(String[] args) {
		   SpringApplication.run(SampleApplication.class, args);
	}

}
```

### WelcomeController

Controller will process "/download" request, run asynchronously  "org.neuro4j.workflow.DownloadPages-Start" and redirect to HystrixDashboard

```

@Controller
public class WelcomeController {
	
	private static final Logger Logger = LoggerFactory.getLogger(WelcomeController.class);

	@Autowired
	private WorkflowEngine engine;

	@RequestMapping("/")
	public String welcome(Map<String, Object> model) {
		return "welcome";
	}

	@RequestMapping("/download")
	public RedirectView download(Map<String, Object> model) {


		
		List<String> urls = new ArrayList<>();
		
		// add many
		for (int i =0; i < 20; i++){
			urls.add("https://en.m.wikipedia.org/wiki/Antarctica/Rothera#/random");
			urls.add("https://en.m.wikipedia.org/wiki/Bengal_tiger");
			urls.add("https://en.m.wikipedia.org/wiki/Bengal");
			urls.add("https://en.wikipedia.org/wiki/List_of_Swedish_monarchs");
			urls.add("https://en.wikipedia.org/wiki/Monarchy_of_Sweden");
			
		}
		
		model.put("urls", urls);
		
		
		try {
			engine.executeAsync("org.neuro4j.workflow.DownloadPages-Start", model);
		} catch (FlowExecutionException e) {
			Logger.error(e.getMessage(), e);
		}

		return new RedirectView("/hystrix/monitor?stream=http%3A%2F%2Flocalhost%3A8080%2Fmetrics%2Fhystrix.stream");
	}
	
}



```

### Workflow

Workflow after `Fork` node will run sub-flows in 3 different threads and will wait at Join node. After Join node it will continue execution in 1 "main" thread.

![hystryx-example-higlevel](https://raw.github.com/neuro4j/workflow/master/doc/images/FlowWithHystrixCommands.png "DownloadPages workflow")

### HystrixCommand DownloadPage

This block defines business functionality and will call DownloadService to get content from remote server.
DownloadPage has `@Component` annotation which allows to use Spring's BeanFactory to initialize all services defined in block (like DownloadService).
Also it uses @Scope("prototype") annotation
```
@ParameterDefinitionList(input = {
		@ParameterDefinition(name = IN_URL, isOptional = true, type = "java.lang.String") }, output = {
				@ParameterDefinition(name = OUT_DOCUMENT, isOptional = true, type = "java.lang.String") })

@Component
@Scope("prototype")
public class DownloadPage extends BaseHystrixCommand {
	
	public DownloadPage(){
		super(getSetterForGroup("TestGroupKey", "TestCommandKey"));
	}

	private static final Logger Logger = LoggerFactory.getLogger(DownloadPage.class);

	static final String IN_URL = "url";

	static final String OUT_DOCUMENT = "document";
	

	@Autowired
	private DownloadService service;

	public Integer executeInternal(FlowContext ctx) throws FlowExecutionException {

		String url = (String) ctx.get(IN_URL);

		Logger.debug("Downloading page {}", url);

		String content = "";

		try {

			content = service.download(url);

		} catch (IOException e) {
			Logger.error("Error during downloading url {}", url, e);
		}

		ctx.put(OUT_DOCUMENT, content);

		return NEXT;
	}


}



```

application.conf file defines settings for HystrixCommand

```
neuro4j{

	 hystrix{
	 
	    TestGroupKey{
	        threadpoolSize = 10
	        queueSize = 10
	        maxWait = 1000
	    }
	 
	 }

}

```



### JsoupDownloadService

Will be initialized by Spring and assigned to DownloadPage block

```
@Service
public class JsoupDownloadService implements DownloadService{

	@Override
	public String download(String url) throws IOException {
		Document doc = Jsoup.connect(url).validateTLSCertificates(false).get();
		if (doc != null) {
			String content = doc.html();
			return content;
		}
		return "";
	}

}

```

HystrixServletDefinitions.java defines path to hystrix.stream and registers HystrixMetricsStreamServlet
```
@Configuration
public class HystrixServletDefinitions {
 
       @Bean(name = "hystrixRegistrationBean")
       public ServletRegistrationBean servletRegistrationBean() {
             ServletRegistrationBean registration = new ServletRegistrationBean(
                           new HystrixMetricsStreamServlet(), "/metrics/hystrix.stream");
             registration.setName("hystrixServlet");
             registration.setLoadOnStartup(1);
             return registration;
       }
}
```

### Running application

Run application with command

```
 mvn clean install spring-boot:run
```

Open http://localhost:8080/ and click on Run link


![result-example-higlevel](https://raw.github.com/neuro4j/workflow/master/doc/images/DashboardResult.png "Dashboard result")

