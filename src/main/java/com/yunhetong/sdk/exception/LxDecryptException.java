package com.yunhetong.sdk.exception;

/**
 * <p>Title: LxDecryptException</p>
 * <p>Description: 解密失败的异常</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: www.yunhetong.com</p>
 *
 * @author wuyiping
 * @version 0.0.1
 */
public class LxDecryptException extends SDKException {
    /**
     * 构造函数
     *
     * @param detail 具体的错误信息
     */
    public LxDecryptException(String detail) {
        super(ErrorEnum.DECRYPTE_ERROR.errorCode, ErrorEnum.DECRYPTE_ERROR.errorMsg);
        this.detail = detail;
    }
}
