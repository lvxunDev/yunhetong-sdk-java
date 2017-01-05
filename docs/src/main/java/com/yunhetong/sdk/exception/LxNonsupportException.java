package com.yunhetong.sdk.exception;

/**
 * <p>Title: LxUnsupportException</p>
 * <p>Description: 算法不支持的异常</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: www.yunhetong.com</p>
 *
 * @author wuyiping
 * @version 0.0.1
 */
public class LxNonsupportException extends SDKException {

    /**
     * 构造方法
     *
     * @param detail 异常的具体信息
     */
    public LxNonsupportException(String detail) {
        super(ErrorEnum.SUPPORT_ERROR.errorCode, ErrorEnum.SUPPORT_ERROR.errorMsg);
        this.detail = detail;
    }
}
