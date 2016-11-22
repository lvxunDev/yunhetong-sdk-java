package exception;

/**
 * <p>Title: LxEncryptException</p>
 * <p>Description: 加密失败的异常</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: www.yunhetong.com</p>
 *
 * @author wuyiping
 * @version 0.0.1
 */
public class LxEncryptException extends SDKException {
    /**
     * 构造方法
     *
     * @param msg 具体的错误信息
     */
    public LxEncryptException(String msg) {
        super(ErrorEnum.ENCRYPTE_ERROR.errorCode, ErrorEnum.ENCRYPTE_ERROR.errorMsg);
        this.detail = msg;
    }
}
