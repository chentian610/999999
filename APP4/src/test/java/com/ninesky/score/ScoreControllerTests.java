package com.ninesky.score;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.ninesky.AbstractContextControllerTests;

//@RunWith(SpringJUnit4ClassRunner.class)
public class ScoreControllerTests extends AbstractContextControllerTests{

	private static String URI="/scoreAction/{action}";
	
	private MockMvc mockMvc;
	
	@Before
	public void setup(){
		this.mockMvc=webAppContextSetup(this.wac).alwaysExpect(status().isOk()).build();
	}
}
