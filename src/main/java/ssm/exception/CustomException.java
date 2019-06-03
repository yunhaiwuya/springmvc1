package ssm.exception;

/**
 * 定义一个简单的异常类(只能获取到异常信息，不能获取除异常以外的数据)
 * @author cjm
 *
 */
public class CustomException extends Exception{

	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//异常信息
    public String message;

    public CustomException(String message) {
        super(message);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
