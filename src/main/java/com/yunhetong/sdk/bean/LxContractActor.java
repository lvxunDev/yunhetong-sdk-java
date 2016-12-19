package com.yunhetong.sdk.bean;


/**
 * <p>Title: LxContractActor</p>
 * <p>Description: 合同参与者类</p>
 * <p>合同参与者我们才不管甲方还是乙方呢，反正有几个参与者就传几个 Actor 就好了</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: www.yunhetong.com</p>
 *
 * @author wuyiping
 * @version 0.0.1
 */
public class LxContractActor extends LxUser {

    /**
     * 是否自动签署：true 是；false或空不是
     */
    private Boolean autoSign;

    /**
     * 签名位置Id
     */
    private String locationName;

    /**
     * 默认的空够着方法，可能没什么用
     */
    private LxContractActor() {
    }

    /**
     * @param appUserId     用户在第三方应用的id，由第三方应用自己管理
     * @param phone         用户的手机号码
     * @param userType      用户类型
     * @param userName      用户名
     * @param certifyType   实名认证类型
     * @param certifyNumber 具体的实名认证证件号码
     * @param autoSign      是否自动签名
     * @param locationName  签名位置，在模板那边设置的
     * @see UserType
     * @see CertifyType
     */
    public LxContractActor(String appUserId, String phone, UserType userType, String userName, CertifyType certifyType, String certifyNumber, Boolean autoSign, String locationName) {
        super(appUserId, phone, userType, userName, certifyType, certifyNumber);
        this.autoSign = autoSign;
        this.locationName = locationName;
    }

    /**
     * 通过原有的 LxUser 来创建合同参与方
     *
     * @param lxUser       lxUser
     * @param autoSign     是否自动签名
     * @param locationName 签名位置，在模板那边设置的
     */
    public LxContractActor(LxUser lxUser, Boolean autoSign, String locationName) {
        super(lxUser.getAppUserId(), lxUser.getPhone(), lxUser.getUserType(), lxUser.getUserName(), lxUser.getCertifyType(), lxUser.getCertifyNumber());
        this.autoSign = autoSign;
        this.locationName = locationName;
    }

    public Boolean getAutoSign() {
        return autoSign;
    }

    public LxContractActor setAutoSign(Boolean autoSign) {
        this.autoSign = autoSign;
        return this;
    }

    public String getLocationName() {
        return locationName;
    }

    public LxContractActor setLocationName(String locationName) {
        this.locationName = locationName;
        return this;
    }
}
