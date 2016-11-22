package exception;

/**
 * <p>Title: LxKeyException</p>
 * <p>Description: 公私玥的异常</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: www.yunhetong.com</p>
 *
 * @author wuyiping
 * @version 0.0.1
 */
public class LxKeyException extends SDKException {

    /**
     * 构造方法
     *
     * @param msg 具体的错误信息
     */
    public LxKeyException(String msg) {
        super(ErrorEnum.KEY_ERROR.errorCode, ErrorEnum.KEY_ERROR.errorMsg);
        this.detail = msg;
    }
}
