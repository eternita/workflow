## Parallel execution in workflow 

Let's create simple SpringBoot application which will download documents from web in parallel way
 
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

### WelcomeController

Controller will process "/download" requests and run "org.neuro4j.workflow.DownloadPages-Start" workflow

```

@Controller
public class WelcomeController {

	@Autowired
	private WorkflowEngine engine;

	@RequestMapping("/")
	public String welcome(Map<String, Object> model) {
		return "welcome";
	}

	@RequestMapping("/download")
	public String download(Map<String, Object> model) {

		WorkflowRequest request = new WorkflowRequest(model);
		
		request.addParameter("url1", "https://en.m.wikipedia.org/wiki/Antarctica/Rothera#/random");
		request.addParameter("url2", "https://en.m.wikipedia.org/wiki/Bengal_tiger");
		request.addParameter("url3", "https://en.m.wikipedia.org/wiki/Bengal");
		
		ExecutionResult result = engine.execute("org.neuro4j.workflow.DownloadPages-Start", request);
		model.putAll(result.getFlowContext().getParameters());
		return "download";
	}
	
}


```

### Workflow

Workflow after `Fork` node will run sub-flows in 3 different threads and will wait at Join node. After Join node it will continue execution in 1 "main" thread.

![parallel-example-higlevel](https://raw.github.com/neuro4j/workflow/master/doc/images/Parallel.png "DownloadPages workflow")

### CustomBlock DownloadPage

This block defines business functionality and will call DownloadService to get content from remote server.
DownloadPage has `@Component` annotation which allows to use Spring's BeanFactory to initialize all services defined in block (like DownloadService).

```
@ParameterDefinitionList(input = {
		@ParameterDefinition(name = IN_URL, isOptional = true, type = "java.lang.String") }, output = {
				@ParameterDefinition(name = OUT_DOCUMENT, isOptional = true, type = "java.lang.String") })
@CachedNode(type = SINGLETON)
@Component
public class DownloadPage implements ActionBlock {

	private static final Logger Logger = LoggerFactory.getLogger(DownloadPage.class);

	static final String IN_URL = "url";

	static final String OUT_DOCUMENT = "document";
	

	@Autowired
	private DownloadService service;

	public int execute(FlowContext ctx) throws FlowExecutionException {

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

### Running application

Run application with command

```
 mvn clean install spring-boot:run
```

Open http://localhost:8080/download


![result-example-higlevel](https://raw.github.com/neuro4j/workflow/master/doc/images/DownloadPagesResult.png "DownloadPages result")

