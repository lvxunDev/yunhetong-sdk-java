package exception;

/**
 * <p>Title: LxVerifyException</p>
 * <p>Description: 签名验证失败的异常</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: www.yunhetong.com</p>
 *
 * @author wuyiping
 * @version 0.0.1
 */
public class LxVerifyException extends SDKException {

    /**
     * 构造方法
     *
     * @param detail 异常的具体信息
     */
    public LxVerifyException(String detail) {
        super(ErrorEnum.VERIFY_ERROR.errorCode, ErrorEnum.VERIFY_ERROR.errorMsg);
        this.detail = detail;
    }
}
