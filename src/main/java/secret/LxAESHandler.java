package secret;

import exception.LxDecryptException;
import exception.LxEncryptException;
import exception.LxKeyException;
import exception.LxUnsupportException;
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
 * <p>Description: 算法不支持的异常</p>
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

    /** AES 秘钥 **/
    private SecretKey secretKey;
    /** AES 加密向量**/
    private byte[] IV;
    /** 秘钥生成时间 **/
    private long birthtime;

    /**
     * <p>构造方法</p>
     * <p>刷新秘钥向量和时间</p>
     * @throws LxUnsupportException
     */
    public LxAESHandler() throws LxUnsupportException {
        refreshKey();
    }

    /**
     * <p>构造方法</p>
     * <p>刷新秘钥向量和时间</p>
     * @param bytes 转化成字节的秘钥
     * @throws LxKeyException
     */
    public LxAESHandler(byte[] bytes) throws LxKeyException {
        this(new String(bytes));
    }

    /**
     * <p>构造方法</p>
     * <p>通过一个有key,iv,bt的json串初始化 aes</p>
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

    private String getSecretKey() {
        return new BASE64Encoder().encodeBuffer(secretKey.getEncoded());
    }

    public String encryptWithUTF8(String __value) throws LxUnsupportException, LxKeyException, LxEncryptException {
        try {
            Cipher __cipher = Cipher.getInstance(ALGORIHM);
            __cipher.init(Cipher.ENCRYPT_MODE, this.secretKey, new IvParameterSpec(this.IV));
            byte[] __data = __cipher.doFinal(__value.getBytes("UTF8"));
            return new BASE64Encoder().encodeBuffer(__data);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new LxUnsupportException(e.getMessage());
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            throw new LxUnsupportException(e.getMessage());
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

    public String decryptWithUTF8(String __value) throws LxUnsupportException, LxKeyException, LxDecryptException {
        try {
            Cipher __cipher = Cipher.getInstance(ALGORIHM);
            __cipher.init(Cipher.DECRYPT_MODE, this.secretKey, new IvParameterSpec(this.IV));
            byte[] __data = __cipher.doFinal(new BASE64Decoder().decodeBuffer(__value));
            return new String(__data, "UTF-8");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new LxUnsupportException(e.getMessage());
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            throw new LxUnsupportException(e.getMessage());
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

    @Override
    public String toString() {
        Map<String, Object> __map = new HashMap<String, Object>();
        __map.put("key", this.getSecretKey());
        __map.put("iv", new BASE64Encoder().encodeBuffer(this.IV));
        __map.put("bt", this.birthtime);
        return new JSONObject(__map).toString();
    }

    @Deprecated
    long timeDifference() {
        return new Date().getTime() - this.birthtime;
    }

    @Deprecated
    void refreshKey() throws LxUnsupportException {
        KeyGenerator __keyGenerator = null;
        try {
            __keyGenerator = KeyGenerator.getInstance("AES");
            __keyGenerator.init(KEY_LENGTH);
            this.secretKey = __keyGenerator.generateKey();
            this.IV = RandomUtil.generateString(KEY_LENGTH / 8).getBytes();
            this.birthtime = new Date().getTime();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new LxUnsupportException(e.getMessage());
        }
    }

    @Deprecated
    public LxAESHandler clone() {
        Object o = null;
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(this);

            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ois = new ObjectInputStream(bais);
            o = ois.readObject();
            return (LxAESHandler) o;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                oos.close();
                ois.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return this;
    }
}
