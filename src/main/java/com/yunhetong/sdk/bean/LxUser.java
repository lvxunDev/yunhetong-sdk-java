package com.yunhetong.sdk.bean;


/**
 * <p>Title: LxUser</p>
 * <p>Description: 用户类</p>
 * <p>用户的一些基本信息</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: www.yunhetong.com</p>
 *
 * @author wuyiping
 * @version 0.0.1
 */
public class LxUser {

    /**
     * 用户在第三方应用平台的唯一标识，由平台各自管理，不能为空，不能大于 200 个字符
     */
    protected String appUserId;

    /**
     * 用户的手机号码
     */
    protected String phone;
    /**
     * 用户类型
     *
     * @see LxUser.UserType
     */
    protected UserType userType;
    /**
     * 用户名，用户实名认证时的用户名，小于 50 个字符
     */
    protected String userName;
    /**
     * 用户实名认证类型
     *
     * @see LxUser.CertifyType
     */
    protected CertifyType certifyType;
    /**
     * 用户实名认证时候的证件号码，可以是对应的身份证、营业执照、组织机构代码证或者其他证件号码，原则上不能大于 50 个字符
     */
    protected String certifyNumber;

    /**
     * 是否自动创建签名
     * 在导入用户并且当值为 1 时，会为导入的用户自动创建签名，0的话就不会，这个值只在用户第一次导入时有效
     */
    protected String createSignature = "0";


    /**
     * 一个空的构造方法，可能并没有什么用
     */
    public LxUser() {
    }

    /**
     * 正经处使用用户的一个方法
     *
     * @param appUserId     用户在你们平台的 id
     * @param phone         用户的手机号
     * @param userType      用户类型
     * @param userName      用户名
     * @param certifyType   实名认证类型
     * @param certifyNumber 实名认证号码
     * @see LxUser.UserType
     * @see LxUser.CertifyType
     */
    public LxUser(String appUserId, String phone, UserType userType, String userName, CertifyType certifyType, String certifyNumber, String createSignature) {
        this.appUserId = appUserId;
        this.phone = phone;
        this.userType = userType;
        this.userName = userName;
        this.certifyType = certifyType;
        this.certifyNumber = certifyNumber;
        this.createSignature = createSignature;
    }

    /**
     * 用户类型
     * 1个人2企业
     */
    public enum UserType {

        USER("普通用户", 1), COMPANY("企业用户", 2);

        private String name;
        private int value;

        private UserType(String name, int value) {
            this.name = name;
            this.value = value;
        }

        public static UserType get(int value) {
            if (value == 1) return USER;
            if (value == 2) return COMPANY;
            return USER;
        }

        public int getValue() {
            return value;
        }
    }

    /**
     * Created by wuyiping on 16/4/23.
     * 证件类型：1身份证,2护照,3军官证,4营业执照,5组织机构代码证,6三证合一的那个证
     */
    public enum CertifyType {
        ID_CARD("身份证", 1), PASSPORT("护照", 2),
        OFFICIAL_CARD("军官证", 3), BUSINESS_LICENCE("营业执照", 4),
        ORGANIZATION_CODE("组织机构代码证", 5), TYPE_6("三证合一", 6);

        private String name;
        private int value;

        public static CertifyType get(int value) {
            if (value == 1) return ID_CARD;
            if (value == 2) return PASSPORT;
            if (value == 3) return OFFICIAL_CARD;
            if (value == 4) return BUSINESS_LICENCE;
            if (value == 5) return ORGANIZATION_CODE;
            if (value == 6) return TYPE_6;
            return ID_CARD;
        }

        private CertifyType(String name, int value) {
            this.name = name;
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }


    public String getAppUserId() {
        return appUserId;
    }

    public LxUser setAppUserId(String appUserId) {
        this.appUserId = appUserId;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public LxUser setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public UserType getUserType() {
        return userType;
    }

    public LxUser setUserType(UserType userType) {
        this.userType = userType;
        return this;
    }

    public String getUserName() {
        return userName;
    }

    public LxUser setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public CertifyType getCertifyType() {
        return certifyType;
    }

    public LxUser setCertifyType(CertifyType certifyType) {
        this.certifyType = certifyType;
        return this;
    }


    public String getCreateSignature() {
        return createSignature;
    }

    public LxUser setCreateSignature(String createSignature) {
        this.createSignature = createSignature;
        return this;
    }

    public String getCertifyNumber() {
        return certifyNumber;
    }

    public LxUser setCertifyNumber(String certifyNumber) {
        this.certifyNumber = certifyNumber;
        return this;
    }
}
