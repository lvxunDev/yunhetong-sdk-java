package com.yunhetong.sdk.secret;

import com.yunhetong.sdk.exception.LxDecryptException;
import com.yunhetong.sdk.exception.LxEncryptException;
import com.yunhetong.sdk.exception.LxKeyException;
import com.yunhetong.sdk.exception.LxNonsupportException;
import org.json.JSONObject;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>Title: LxAESHandler</p>
 * <p>Description: AES加密的相关算法 </p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: www.yunhetong.com</p>
 *
 * @author wuyiping
 * @version 0.0.1
 */
public class LxAESHandler implements Serializable {

    /**
     * aes 填充模式
     **/
    private static final String ALGORIHM = "AES/CBC/PKCS5Padding";
    /**
     * 秘钥长度
     **/
    private static final int KEY_LENGTH = 128;

    /**
     * AES 秘钥
     **/
    private SecretKey secretKey;
    /**
     * AES 加密向量
     **/
    private byte[] IV;
    /**
     * 秘钥生成时间
     **/
    private long birthtime;

    /**
     * <p>构造方法</p>
     * <p>刷新秘钥向量和时间</p>
     *
     * @throws LxNonsupportException
     */
    public LxAESHandler() throws LxNonsupportException {
        refreshKey();
    }

    /**
     * <p>构造方法</p>
     * <p>刷新秘钥向量和时间</p>
     *
     * @param bytes 转化成字节的秘钥
     * @throws LxKeyException
     */
    public LxAESHandler(byte[] bytes) throws LxKeyException {
        this(new String(bytes));
    }

    /**
     * <p>构造方法</p>
     * <p>通过一个有key,iv,bt的json串初始化 aes</p>
     *
     * @param json 有着key，iv,bt 的 json 串
     * @throws LxKeyException
     */
    public LxAESHandler(String json) throws LxKeyException {
        JSONObject jsonObject = new JSONObject(json);
        String key = (String) jsonObject.get("key");
        try {
            this.secretKey = new SecretKeySpec(new BASE64Decoder().decodeBuffer(key), "AES");
            String iv = (String) jsonObject.get("iv");
            this.IV = new BASE64Decoder().decodeBuffer(iv);
            this.birthtime = (Long) jsonObject.get("bt");
        } catch (IOException e) {
            e.printStackTrace();
            throw new LxKeyException(e.getMessage());
        }
    }

    /**
     * 获取 AES 的 key
     *
     * @return 返回 base64 之后的 key
     */
    private String getSecretKey() {
        return new BASE64Encoder().encodeBuffer(secretKey.getEncoded());
    }

    /**
     * AES 加密方法
     *
     * @param value 要加密的值
     * @return 加密之后再 base64 的值
     * @throws LxNonsupportException
     * @throws LxKeyException
     * @throws LxEncryptException
     */
    public String encryptWithUTF8(String value) throws LxNonsupportException, LxKeyException, LxEncryptException {
        try {
            Cipher __cipher = Cipher.getInstance(ALGORIHM);
            __cipher.init(Cipher.ENCRYPT_MODE, this.secretKey, new IvParameterSpec(this.IV));
            byte[] __data = __cipher.doFinal(value.getBytes("UTF8"));
            return new BASE64Encoder().encodeBuffer(__data);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new LxNonsupportException(e.getMessage());
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            throw new LxNonsupportException(e.getMessage());
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            throw new LxKeyException(e.getMessage());
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
            throw new LxEncryptException(e.getMessage());
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
            throw new LxEncryptException(e.getMessage());
        } catch (BadPaddingException e) {
            e.printStackTrace();
            throw new LxEncryptException(e.getMessage());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new LxEncryptException(e.getMessage());
        }


    }

    /**
     * AES 解密
     *
     * @param value 要解密的字符串
     *              该字符串是 AES 加密之后再 base64 之后的字符串
     * @return 返回解密之后的明文
     * @throws LxNonsupportException
     * @throws LxKeyException
     * @throws LxDecryptException
     */
    public String decryptWithUTF8(String value) throws LxNonsupportException, LxKeyException, LxDecryptException {
        try {
            Cipher cipher = Cipher.getInstance(ALGORIHM);
            cipher.init(Cipher.DECRYPT_MODE, this.secretKey, new IvParameterSpec(this.IV));
            byte[] data = cipher.doFinal(new BASE64Decoder().decodeBuffer(value));
            return new String(data, "UTF-8");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new LxNonsupportException(e.getMessage());
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            throw new LxNonsupportException(e.getMessage());
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            throw new LxKeyException(e.getMessage());
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
            throw new LxDecryptException(e.getMessage());
        } catch (BadPaddingException e) {
            e.printStackTrace();
            throw new LxDecryptException(e.getMessage());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new LxDecryptException(e.getMessage());
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
            throw new LxDecryptException(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            throw new LxDecryptException(e.getMessage());
        }

    }

    /**
     * @return 返回 json 格式的 {"key":"xxx","iv":"xxx","bt":"xxx" }
     */
    @Override
    public String toString() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("key", this.getSecretKey());
        map.put("iv", new BASE64Encoder().encodeBuffer(this.IV));
        map.put("bt", this.birthtime);
        return new JSONObject(map).toString();
    }

    /**
     * 算时间差的方法
     *
     * @return 返回 现在到 birthtime 到现在的时间差
     */
    @Deprecated
    long timeDifference() {
        return new Date().getTime() - this.birthtime;
    }

    /**
     * 刷新 aes 的 key,iv 和 birthtime
     *
     * @throws LxNonsupportException
     */
    @Deprecated
    void refreshKey() throws LxNonsupportException {
    /*  这里本来用的是 KeyGenerator.getInstance("AES").init(KEY_LENGTH); 的，但是在.Net 那边会迷之报错，改成这样就不报错了，等以后的大牛解决，现在就先这样吧*/
        this.secretKey = new SecretKeySpec(RandomUtil.generateString(16).getBytes(), "AES");
        this.IV = RandomUtil.generateString(KEY_LENGTH / 8).getBytes();
        this.birthtime = new Date().getTime();

    }
}
