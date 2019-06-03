package ssm.controller;

import java.io.File;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import ssm.exception.CustomException;
import ssm.po.ItemsCustom;
import ssm.po.ItemsQueryVo;
import ssm.service.ItemsService;
import ssm.validator.ValidGroup1;

@Controller
public class ItemsController {

	@Autowired
	private ItemsService itemsService;

	@RequestMapping("/queryItems")
	public ModelAndView queryItems(String id) throws Exception {

		//调用service查找数据库，查询商品列表
        //这里传入进去一个null表示没有附加条件，查询所有的。因为service中接收的是一个ItemsQueryVo对象
        List<ItemsCustom> itemsList = itemsService.findItemsList(null);

        //返回ModelAndView
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("itemsList", itemsList);
        modelAndView.setViewName("/WEB-INF/jsp/items/itemsList.jsp");
        return modelAndView;
	}

	@RequestMapping(value="/editItems")
	public String editItems(Model model,@RequestParam("id") Integer item_id) throws CustomException{
		ItemsCustom items = itemsService.findItemsById(item_id);
		
		if(items==null){
			throw new CustomException("修改商品的信息不存在！");
		}
		model.addAttribute("itemsCustom", items);
		
		//通过形参中的model将model数据传到页面
	    //相当于modelAndView.addObject方法
	    return "/WEB-INF/jsp/items/editItems.jsp";
	}
	
	@RequestMapping(value="/editItemsSubmit")
	//@ResponseBody 表示只校验ValidGroup1 该分组的字段
	public String editItemsSubmit(Model model,HttpServletRequest request,
			Integer id,
			@Validated(value={ValidGroup1.class}) ItemsCustom itemsCustom,
			BindingResult bindingResult,
//			MultipartFile items_pic 单个文件上传
			@RequestParam MultipartFile[] items_pic)
	  throws Exception{
		System.out.println(id+"====="+itemsCustom.getName());
		//获取校验错误信息
		if(bindingResult.hasErrors()){
			List<ObjectError> allErrors = bindingResult.getAllErrors();
			for(ObjectError error:allErrors){
				System.out.println(new String(error.getDefaultMessage().getBytes("ISO-8859-1"),"UTF-8"));
			}
			//输出错误信息到页面
			model.addAttribute("allErrors", allErrors);
			return "/WEB-INF/jsp/items/editItems.jsp";
		}
		
		/*// 处理上传的单个图片    
	    String originalFileName = items_pic.getOriginalFilename();// 原始名称
	    // 上传图片
	    if (items_pic != null && originalFileName != null && originalFileName.length() > 0) {
	        // 存储图片的物理路径，实际中是要写到配置文件中的，不能在这写死
	        String pic_path = "D:\\upload\\img\\";
	        // 新的图片名称
	        String newFileName = UUID.randomUUID()
	                + originalFileName.substring(originalFileName
	                        .lastIndexOf("."));     
	        File newFile = new File(pic_path + newFileName);//新图片   
	        items_pic.transferTo(newFile);// 将内存中的数据写入磁盘
	        itemsCustom.setPic(newFileName);// 将新图片名称写到itemsCustom中
	    } else {
	        //如果用户没有选择图片就上传了，还用原来的图片
	        ItemsCustom temp = itemsService.findItemsById(itemsCustom.getId());
	        itemsCustom.setPic(temp.getPic());
	    }*/
		
		//多个图片，不存数据库了，在此打印一下即可
	    for(MultipartFile myfile : items_pic) {
	        if(myfile.isEmpty()){  
	            System.out.println("文件未上传");  
	        }else{  
	            System.out.println("文件长度: " + myfile.getSize());  
	            System.out.println("文件类型: " + myfile.getContentType());  
	            System.out.println("文件名称: " + myfile.getName());  
	            System.out.println("文件原名: " + myfile.getOriginalFilename());  
	            System.out.println("========================================");  

	            //写入磁盘，和上面的单个文件上传一模一样
	            String originalFileName = myfile.getOriginalFilename();
	            String pic_path = "D:\\upload\\img\\";
	            String newFileName = UUID.randomUUID()
	                    + originalFileName.substring(originalFileName
	                            .lastIndexOf("."));
	            File newFile = new File(pic_path + newFileName);
	            myfile.transferTo(newFile);
	            itemsCustom.setPic(newFileName);
	        }  
	    }   
	
		itemsService.updateItems(id,itemsCustom);
		//通过形参中的model将model数据传到页面
	    //相当于modelAndView.addObject方法
	    return "/WEB-INF/jsp/success.jsp";
	}
	
	@RequestMapping(value="/editItemsAllSubmit")
	//@ResponseBody
	public String editItemsAllSubmit(Model model,ItemsQueryVo itemsQueryVo)
	  throws Exception{
		System.out.println("====="+itemsQueryVo.getItemsCustom().getName());
		List<ItemsCustom> itemsList = itemsService.findItemsList(itemsQueryVo);
		
		//通过形参中的model将model数据传到页面
		model.addAttribute("itemsList", itemsList);
	    //相当于modelAndView.addObject方法
	    return "/WEB-INF/jsp/items/itemsList.jsp";
	}
	
	//前台checkbox传递数组items_id
	@RequestMapping(value="/deleteItems")
	public String deleteItems(Integer[] items_id) throws Exception{
		//先不删，
		if(items_id!=null){
			System.out.println(items_id.length);
		}
		return "/WEB-INF/jsp/success.jsp";
	}
	
   // 批量修改商品页面，将商品信息查询出来，在页面中可以编辑商品信息
	@RequestMapping("/editItemsQuery")
	public ModelAndView editItemsQuery(HttpServletRequest request,
			ItemsQueryVo itemsQueryVo) throws Exception {

		// 调用service查找 数据库，查询商品列表
		List<ItemsCustom> itemsList = itemsService.findItemsList(itemsQueryVo);

		// 返回ModelAndView
		ModelAndView modelAndView = new ModelAndView();
		// 相当 于request的setAttribut，在jsp页面中通过itemsList取数据
		modelAndView.addObject("itemsList", itemsList);

		modelAndView.setViewName("/WEB-INF/jsp/items/editItemsQuery.jsp");

		return modelAndView;
	}
	//批量修改成功后跳转的页面
	@RequestMapping(value="/editItemsQueryResult")
	public String editItemsQueryResult(ItemsQueryVo itemsQueryVo) throws Exception{
		//先不删，
		System.out.println(itemsQueryVo.getItemsList().size());
		return "/WEB-INF/jsp/success.jsp";
	}
	//测试请求的是json串(商品信息)，输出json(商品信息)
	//@RequestBody将请求的商品信息的json串转成itemsCustom对象
	//@ResponseBody将itemsCustom对象转成json输出
	@RequestMapping("/requestJson")
	@ResponseBody
	public ItemsCustom requestJson(@RequestBody ItemsCustom itemsCustom) {
		
		return itemsCustom; //由于@ResponseBody注解，将itemsCustom转成json格式返回
	}
	
	//测试请求的是key/value(商品信息)，输出json(商品信息)
	//@ResponseBody将itemsCustom对象转成json输出
	@RequestMapping("/responseJson")
	@ResponseBody
	public ItemsCustom responseJson(ItemsCustom itemsCustom) {
		
		return itemsCustom; //由于@ResponseBody注解，将itemsCustom转成json格式返回
	}
	
	//查询商品信息，输出json，使用RESTful
	@RequestMapping("/itemsView/{id}")
	public @ResponseBody ItemsCustom itemsView(@PathVariable("id") Integer id) throws Exception {
		ItemsCustom itemsCustom = itemsService.findItemsById(id);
		return itemsCustom;
	}
	
	//登陆
	@RequestMapping("/login")
	public String login(HttpServletRequest request, String username, String password) throws Exception {
		
		//实际中要去和数据库匹配的
		//....
		HttpSession session = request.getSession();
		session.setAttribute("username", username);
		return "redirect:queryItems.action";
	}
	
	//退出
	@RequestMapping("/logout")
	public String logout(HttpServletRequest request) throws Exception {
		
		
		HttpSession session = request.getSession();
		session.invalidate();
		return "redirect:queryItems.action";
	}
}
