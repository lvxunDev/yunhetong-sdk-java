package com.yunhetong.sdk.exception;

/**
 * <p>Title: LxSignatureException</p>
 * <p>Description: 签名失败的异常</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: www.yunhetong.com</p>
 *
 * @author wuyiping
 * @version 0.0.1
 */
public class LxSignatureException extends SDKException {

    /**
     * 构造方法
     *
     * @param detail 异常的具体信息
     */
    public LxSignatureException(String detail) {
        super(ErrorEnum.SIGN_ERROR.errorCode, ErrorEnum.SIGN_ERROR.errorMsg);
        this.detail = detail;
    }
}
