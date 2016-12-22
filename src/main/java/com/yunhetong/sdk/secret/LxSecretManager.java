package com.yunhetong.sdk.secret;

import com.yunhetong.sdk.exception.*;
import org.json.JSONObject;

import java.io.InputStream;

//
//                       _oo0oo_
//                      o8888888o
//                      88" . "88
//                      (| -_- |)
//                      0\  =  /0
//                    _/`---'\_
//                  .' \\|     |// '.
//                 / \\|||  :  |||// \
//                / _||||| -:- |||||- \
//               |   | \\\  -  /// |   |
//               | \_|  ''\---/''  |_/ |
//               \  .-\  '-'  _/-. /
//             _'. .'  /--.--\  `. .'_
//          ."" '<  `._\_<|>_/_.' >' "".
//         | | :  `- \`.;`\ _ /`;.`/ - ` : | |
//         \  \ `_.   \_ \ / _/   .-` /  /
//     =====`-.`._ \_/_.-`_.-'=====
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
 * <p>Title: LxSecretManager</p>
 * <p>Description: 云合同秘钥管理的相关方法 </p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: www.yunhetong.com</p>
 *
 * @author wuyiping
 * @version 0.0.1
 */
public final class LxSecretManager {

    /**
     * RSA 的相关方法
     */
    private LxRSAHandler rsaHandler;

    /**
     * 构造方法
     * 主要是初始化了 RSA
     *
     * @param publicStream  公钥的文件流
     * @param privateStream 私钥的文件流
     * @throws LxKeyException
     * @throws LxNonsupportException
     */
    public LxSecretManager(InputStream publicStream, InputStream privateStream) throws LxKeyException, LxNonsupportException {
        rsaHandler = new LxRSAHandler();
        rsaHandler.initPublicKey4Stream(publicStream);
        rsaHandler.initPrivateKey4Stream(privateStream);
    }

    /**
     * 加密字符串的方法
     * 主要的过程是先生成 AES 的秘钥，然后用秘钥加密明文，在用 RSA 加密 AES 的秘钥
     *
     * @param json 要加密的字符串，主要是 json 格式
     * @return 返回加密之后的密文和签名
     * @throws LxNonsupportException
     * @throws LxEncryptException
     * @throws LxKeyException
     * @throws LxSignatureException
     */
    public String encryptWithUTF8(String json) throws LxNonsupportException, LxEncryptException, LxKeyException, LxSignatureException {

        LxAESHandler aes = new LxAESHandler();
        String sessionKey = rsaHandler.encryptRSAWithUTF8(aes.toString());
        String secret = aes.encryptWithUTF8(json);
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("key", sessionKey);
        jsonObj.put("content", secret);
        jsonObj.put("sign", aes.encryptWithUTF8(rsaHandler.signWithUTF8(json)));
        return jsonObj.toString();
    }

    /**
     * 解密字符串的方法
     * 主要过程是先用 RSA 解密 AES 的秘钥，然后在用AES 的秘钥去解出密文
     *
     * @param json 要解密的字符串
     * @return 返回解密之后的内容
     * @throws LxDecryptException
     * @throws LxNonsupportException
     * @throws LxKeyException
     * @throws LxVerifyException
     */
    public String decryptWithUTF8(String json) throws LxDecryptException, LxNonsupportException, LxKeyException, LxVerifyException {
        try {
            JSONObject jsonObject = new JSONObject(json);
            String sessionKey = jsonObject.getString("key");
            sessionKey = rsaHandler.decryptRSAWithUTF8(sessionKey);
            LxAESHandler aes = new LxAESHandler(sessionKey);
            String content = aes.decryptWithUTF8(jsonObject.getString("content"));
            String sign = aes.decryptWithUTF8(jsonObject.getString("sign"));
            if (rsaHandler.verifyWithUTF8(content, sign)) {
                return content;
            } else {
                throw new LxVerifyException("sign's validation is error when decrypted");
            }

        } catch (org.json.JSONException e) {
            return json;
        }
    }

    /**
     * 对内容进行签名
     *
     * @param content 要签名的内容
     * @return 返回签名之后的内容
     * @throws LxNonsupportException
     * @throws LxSignatureException
     * @throws LxKeyException
     */
    public String signWithUTF8(String content) throws LxNonsupportException, LxSignatureException, LxKeyException {
        return this.rsaHandler.signWithUTF8(content);
    }

    public boolean verifyWithUTF8(String content, String sign) throws LxVerifyException, LxKeyException, LxNonsupportException {
        return this.rsaHandler.verifyWithUTF8(content, sign);
    }

}
