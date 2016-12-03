package bean;

/**
 * Created by wuyiping on 16/4/23.
 */
public class LxContractActor extends LxUser {

    /**
     * 是否自动签署：1是；0或空不是
     */
    public Boolean autoSign;

    /**
     * 签名位置Id
     */
    public String locationName;

    public LxContractActor() {
    }

    /**
     * @param userId
     * @param phone
     * @param userType
     * @param userName
     * @param certifyType
     * @param certifyNumber
     * @param autoSign
     * @param locationName
     */
    public LxContractActor(String userId, String phone, UserType userType, String userName, CertifyType certifyType, String certifyNumber, Boolean autoSign, String locationName) {
        super(userId, phone, userType, userName, certifyType, certifyNumber);
        this.autoSign = autoSign;
        this.locationName = locationName;
    }
}
