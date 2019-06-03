package ssm.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

/**
 * 自定义全局异常类
 * 1、解析出异常类型
 * 2、如果该异常类型是系统自定义的异常，直接取出异常信息，在错误页面展示
 * 3、如果该异常类型不是系统自定义的异常，构造一个自定义的异常类型（信息为“未知错误”）
 * @author cjm
 *
 */
public class CustomExceptionResolver implements HandlerExceptionResolver {

	@Override
	public ModelAndView resolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {
		
		ex.printStackTrace();
		CustomException customException = null;
		
		//如果抛出的是系统自定义的异常则直接转换
		if(ex instanceof CustomException) {
			customException = (CustomException) ex;
		} else {
			//如果抛出的不是系统自定义的异常则重新构造一个未知错误异常
            //这里我就也有CustomException省事了，实际中应该要再定义一个新的异常
			customException = new CustomException("ϵͳδ֪����");
		}

		//向前台返回错误信息
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("message", customException.getMessage());
		modelAndView.setViewName("/WEB-INF/jsp/error.jsp");
		
		return modelAndView;
	}

}
