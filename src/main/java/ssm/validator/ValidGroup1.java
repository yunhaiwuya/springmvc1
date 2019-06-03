package ssm.validator;

/**
 * 定义校验分组 为了能够让不同的controller对同一字段使用不同的校验规则（有的校验，有的不校验）
 * @author cjm
 *
 */
public interface ValidGroup1 {

	//接口中不需要定义任何方法，仅仅是对不同的校验规则进行分组
    //此分组只校验商品名称的长度
}
