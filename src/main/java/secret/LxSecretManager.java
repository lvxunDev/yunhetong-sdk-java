package secret;

import com.yunhetong.sdk.util.exception.*;
import org.json.JSONObject;

import java.io.InputStream;

//
//                       _oo0oo_
//                      o8888888o
//                      88" . "88
//                      (| -_- |)
//                      0\  =  /0
//                    ___/`---'\___
//                  .' \\|     |// '.
//                 / \\|||  :  |||// \
//                / _||||| -:- |||||- \
//               |   | \\\  -  /// |   |
//               | \_|  ''\---/''  |_/ |
//               \  .-\__  '-'  ___/-. /
//             ___'. .'  /--.--\  `. .'___
//          ."" '<  `.___\_<|>_/___.' >' "".
//         | | :  `- \`.;`\ _ /`;.`/ - ` : | |
//         \  \ `_.   \_ __\ /__ _/   .-` /  /
//     =====`-.____`.___ \_____/___.-`___.-'=====
//                       `=---='
//
//
//     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//
//               佛祖保佑         永无BUG
//
//
//


/**
 * Created by wuyiping on 16/2/27.
 */
public final class LxSecretManager implements Cloneable{

    private LxRSAHandler rsaHandler;

    LxSecretManager() {
        super();
        rsaHandler = new LxRSAHandler();
    }

    LxSecretManager(LxRSAHandler rsaHandler) {
        this.rsaHandler = rsaHandler;
    }

    public LxSecretManager(InputStream __publicStream, InputStream __privateStream) throws LxKeyException, LxUnsupportException {
        this();
        rsaHandler.initPublicKey4Stream(__publicStream);
        rsaHandler.initPrivateKey4Stream(__privateStream);
    }
    
    public LxSecretManager clone(){
        return new LxSecretManager(this.rsaHandler);
    }

    public String encryptWithUTF8(String __json) throws LxUnsupportException, LxEncrypteException, LxKeyException, LxSignatureException {

        LxAESHandler __aes = new LxAESHandler();
        String __sessionKey = rsaHandler.encryptRSAWithUTF8(__aes.toString());
        String __secret = __aes.encryptWithUTF8(__json);
        JSONObject __jsonObj = new JSONObject();
        __jsonObj.put("key", __sessionKey);
        __jsonObj.put("content", __secret);
        __jsonObj.put("sign", __aes.encryptWithUTF8(rsaHandler.signWithUTF8(__json)));
        return __jsonObj.toString();
    }

    public String decryptWithUTF8(String __json) throws LxDecryptException, LxUnsupportException, LxKeyException, LxVerifyException {
        try {
            JSONObject __jsonObject = new JSONObject(__json);
            String __sessionKey = __jsonObject.getString("key");
            __sessionKey = rsaHandler.decryptRSAWithUTF8(__sessionKey);
            LxAESHandler __aes = new LxAESHandler(__sessionKey);
            String __content = __aes.decryptWithUTF8(__jsonObject.getString("content"));
            String __sign = __aes.decryptWithUTF8(__jsonObject.getString("sign"));
            if(rsaHandler.verifyWithUTF8(__content, __sign)){
                return __content;
            }else{
                throw new LxVerifyException("sign's validation is error when decrypted");
            }

        } catch (org.json.JSONException e) {
            return __json;
        }
    }

    public String signWithUTF8(String __content) throws LxUnsupportException, LxSignatureException, LxKeyException {
        return this.rsaHandler.signWithUTF8(__content);
    }

    public boolean verifyWithUTF8(String __content, String __sign) throws LxVerifyException, LxKeyException, LxUnsupportException {
        return this.rsaHandler.verifyWithUTF8(__content, __sign);
    }

}
