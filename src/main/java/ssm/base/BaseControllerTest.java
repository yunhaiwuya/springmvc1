package ssm.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;

/*@RunWith(SpringJUnit4ClassRunner.class)*/
@WebAppConfiguration
@ContextConfiguration({"classpath:spring/spring-mvc.xml", "classpath:spring/applicationContext-transaction.xml",
		"classpath:applicationContext-service.xml","classpath:applicationContext-dao.xml" })
public class BaseControllerTest {

	@Autowired
	protected WebApplicationContext wac;
}
