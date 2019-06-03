package springmvc_study;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import ssm.mapper.ItemsCustomMapper;
import ssm.mapper.ItemsMapper;
import ssm.po.Items;
import ssm.po.ItemsCustom;
import ssm.po.ItemsQueryVo;

public class ItemsMapperTest {

	ApplicationContext context = null;
	
	@Before
	public void setUp() throws Exception{
		context = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-dao.xml");
	}
	
	public void selectByPrimaryKey(){
		ItemsMapper mapper = (ItemsMapper) context.getBean("itemsMapper");
		Items items = mapper.selectByPrimaryKey(1);
		System.out.println(items);
	}
	
	
	public void testFindItemsById() throws Exception{
		ItemsCustomMapper itemsMapper = (ItemsCustomMapper) context.getBean("itemsCustomMapper");

        ItemsCustom itemsCustom = new ItemsCustom();
        itemsCustom = itemsMapper.findItemsById(1);
        System.out.println(itemsCustom);

	}
	
	@Test
	public void testFindItemsList() throws Exception{
		ItemsCustomMapper itemsMapper = (ItemsCustomMapper) context.getBean("itemsCustomMapper");

        ItemsQueryVo itemsQueryVo = new ItemsQueryVo();
        ItemsCustom itemsCustom = new ItemsCustom();
        itemsCustom.setName("手机");
        itemsQueryVo.setItemsCustom(itemsCustom);
        List<ItemsCustom> itemsCustomList = itemsMapper.findItemsList(itemsQueryVo);
        System.out.println(itemsCustomList);
	}
	
}
