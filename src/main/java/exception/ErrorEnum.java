package exception;

/**
 * <p>Title: ErrorEnum</p>
 * <p>Description: 一些错误信息的枚举</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: www.yunhetong.com</p>
 *
 * @author wuyiping
 * @author Sean Wu
 * @version 0.0.1
 */
enum ErrorEnum {

    /**
     * 秘钥加载失败
     */
    KEY_ERROR(10601, "秘钥加载失败"),
    /**
     * 算法不支持
     */
    SUPPORT_ERROR(10602, "算法不支持"),
    /**
     * 加密失败
     */
    ENCRYPTE_ERROR(10603, "加密失败"),
    /**
     * 解密失败
     */
    DECRYPTE_ERROR(10604, "解密失败"),
    /**
     * 签名失败
     */
    SIGN_ERROR(10605, "签名失败"),
    /**
     * 验证签名失败
     */
    VERIFY_ERROR(10606, "验证签名失败");

    /**
     * 错误码
     */
    public int errorCode;
    /**
     * 具体的错误信息
     */
    public String errorMsg;

    ErrorEnum(int errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }
}
