package bean;

/**
 * Created by wuyiping on 16/3/2.
 */
public class LxUser {

    public String userId;
    public String phone;
    public UserType userType;
    public String userName;
    public CertifyType certifyType;
    public String certifyNumber;

    public LxUser() {
    }

    /**
     * @param userId
     * @param phone
     * @param userType
     * @param userName
     * @param certifyType
     * @param certifyNumber
     */
    public LxUser(String userId, String phone, UserType userType, String userName, CertifyType certifyType, String certifyNumber) {
        this.userId = userId;
        this.phone = phone;
        this.userType = userType;
        this.userName = userName;
        this.certifyType = certifyType;
        this.certifyNumber = certifyNumber;
    }

    public enum UserType {

        USER("普通用户", 1), Enterprise("企业用户", 2);

        private String name;
        private int value;

        private UserType(String name, int value) {
            this.name = name;
            this.value = value;
        }

        public static UserType get(int value) {
            if (value == 1) return USER;
            if (value == 2) return Enterprise;
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
        TYPE_1("身份证", 1), TYPE_2("护照", 2),
        TYPE_3("军官证", 3), TYPE_4("营业执照", 4),
        TYPE_5("组织机构代码证", 5), TYPE_6("三证合一", 6);

        private String name;
        private int value;

        public static CertifyType get(int value) {
            if (value == 1) return TYPE_1;
            if (value == 2) return TYPE_2;
            if (value == 3) return TYPE_3;
            if (value == 4) return TYPE_4;
            if (value == 5) return TYPE_5;
            if (value == 6) return TYPE_6;
            return TYPE_1;
        }

        private CertifyType(String name, int value) {
            this.name = name;
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}
