package org.neuro4j.springmvc;

import org.junit.Before;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;


public class BaseTestMvc {

    protected MockMvc mockMvc;
    
    protected ResultActions performAsync(RequestBuilder r) throws Exception {
        return mockMvc.perform(asyncDispatch(mockMvc.perform(r).andReturn()));
    };
    
    @Autowired
    protected WebApplicationContext wac;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = webAppContextSetup(wac)
            .build();
    }
	
}
