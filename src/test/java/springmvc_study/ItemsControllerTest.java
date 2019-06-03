package springmvc_study;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import javax.servlet.ServletContext;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import ssm.base.BaseControllerTest;

/**
 * 
 * @author Kemin
 */
@RunWith(SpringJUnit4ClassRunner.class)
// 配置事务的回滚,对数据库的增删改都会回滚,便于测试用例的循环利用
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
public class ItemsControllerTest extends BaseControllerTest{

	private MockMvc mockMvc;
	private String listUrl = "/book/list";
	private String detailUrl = "/book/{bookId}/detail";
	private String appointUrl = "/book/{bookId}/appoint";
	private long bookId = 1000;
	
	@Autowired
	ServletContext context;
	@Autowired
	ItemsControllerTest BookControllerTest;
	
	@Before
	public void setup() {
//		this.mockMvc = webAppContextSetup(this.wac).alwaysExpect(status().isOk()).alwaysDo(print()).build();
		this.mockMvc = MockMvcBuilders.standaloneSetup(context).build();
	}

	
	@Test
	public void list() throws Exception {
		this.mockMvc.perform(get(listUrl)).andExpect(view().name("list"));
	}

	
	public void existDetail() throws Exception {
		this.mockMvc.perform(get(detailUrl, bookId)).andExpect(view().name("detail"))
				.andExpect(model().attributeExists("book"));
	}

	
	public void notExistDetail() throws Exception {
		this.mockMvc.perform(get(detailUrl, 1100)).andExpect(forwardedUrl("/book/list"));
	}

	
	public void appointTest() throws Exception {
		this.mockMvc.perform(post(appointUrl, bookId).param("studentId", "1").accept(MediaType.APPLICATION_JSON))
				.andExpect(content().contentType("application/json;charset=utf-8"));
	}
}
