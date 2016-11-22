package exception;

/**
 * <p>Title: SDKException</p>
 * <p>Description: 接口参数异常</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: www.yunhetong.com</p>
 * @author jgh
 * @version 0.0.1
 */
public class ThirdParamException extends SDKException {
    /**
     * 异常构造方法，必须有Code和msg
     *
     * @param code 状态码
     * @param msg  错误信息
     */
    public ThirdParamException(int code, String msg) {
        super(code, msg);
    }
}
