package ssm.exception;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 使用@ExceptionHandler注解声明异常处理
 * 所有需要异常处理的Controller都继承这个BaseController
 * 从执行来看，不需要配置什么东西，但是代码有侵入性，
 * 需要异常处理的Controller都要继承它才行。 
 * @author cjm
 *
 */
public class BaseControllerException {

	 @ExceptionHandler  
    public String exp(HttpServletRequest request, Exception ex) { 
    //异常处理
    //......
		 return "";
    }
}
