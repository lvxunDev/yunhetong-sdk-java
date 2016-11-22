package exception;

/**
 * <p>Title: SDKException</p>
 * <p>Description: 所有异常的抽象类，sdk的业务异常要继承他~</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: www.yunhetong.com</p>
 *
 * @author fengxx
 * @version 0.0.1
 */
public abstract class SDKException extends Throwable {
    protected int code;
    protected String msg;
    public String detail;

    /**
     * 异常构造方法，必须有Code和msg
     *
     * @param code 状态码
     * @param msg  错误信息
     */
    public SDKException(int code, String msg) {
        super();
        this.code = code;
        this.msg = msg;
    }

    /**
     * @return this.code
     */
    public int getCode() {
        return code;
    }

    /**
     * @return this.msg
     */
    public String getMsg() {
        return this.msg;
    }
}
