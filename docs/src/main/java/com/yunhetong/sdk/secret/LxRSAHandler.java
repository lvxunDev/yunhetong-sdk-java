package com.yunhetong.sdk.secret;

import com.yunhetong.sdk.exception.*;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>Title: LxRSAHandler</p>
 * <p>Description: RSA 加密的相关算法 </p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: www.yunhetong.com</p>
 *
 * @author wuyiping
 * @version 0.0.1
 */
final class LxRSAHandler {

    /**
     * RSA 公钥
     */
    Key publicKey;
    /**
     * RSA 私钥
     */
    Key privateKey;

    private static final int KEY_LENGTH = 1024;

    /**
     * 初始化 RSA 的公钥信息
     *
     * @param stream 公钥文件流
     * @throws LxKeyException
     * @throws LxNonsupportException
     */
    void initPublicKey4Stream(InputStream stream) throws LxKeyException, LxNonsupportException {
        try {
            publicKey = LxRSAHandler.publicKey4String(readStream(stream));
        } catch (IOException e) {
            e.printStackTrace();
            throw new LxKeyException(e.getMessage());
        }
    }


    /**
     * 初始化 RSA 的私钥信息
     *
     * @param stream 私钥文件流
     * @throws LxKeyException
     * @throws LxNonsupportException
     */
    void initPrivateKey4Stream(InputStream stream) throws LxKeyException, LxNonsupportException {
        try {
            privateKey = LxRSAHandler.privateKey4String(readStream(stream));
        } catch (IOException e) {
            e.printStackTrace();
            throw new LxKeyException(e.getMessage());
        }
    }

    /**
     * 将输入流以字符串的形式读出来
     *
     * @param stream 输入流
     * @return 输入流的字符串(UTF8)
     * @throws IOException
     */
    private static String readStream(InputStream stream) throws IOException {
        InputStreamReader read = null;
        BufferedReader bufferedreader = null;
        StringBuilder sb = null;
        try {
            read = new InputStreamReader(stream);
            bufferedreader = new BufferedReader(read);
            sb = new StringBuilder();
            String readline;
            while ((readline = bufferedreader.readLine()) != null) {
                sb.append(readline);
            }
        } finally {
            if (bufferedreader != null) bufferedreader.close();
            if (read != null) read.close();
        }
        return sb.toString();
    }

    /**
     * RSA 加密字符串
     *
     * @param data 要加密的字符串
     * @return 加密之后再 base64 过的密文
     * @throws LxNonsupportException
     * @throws LxKeyException
     * @throws LxEncryptException
     */
    String encryptRSAWithUTF8(String data) throws LxNonsupportException, LxKeyException, LxEncryptException {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return new BASE64Encoder().encode(cipher.doFinal(data.getBytes("UTF-8")));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new LxNonsupportException(e.getMessage());
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            throw new LxNonsupportException(e.getMessage());
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            throw new LxKeyException(e.getMessage());
        } catch (BadPaddingException e) {
            e.printStackTrace();
            throw new LxEncryptException(e.getMessage());
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
            throw new LxEncryptException(e.getMessage());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new LxEncryptException(e.getMessage());
        }
    }

    /**
     * RSA 解密的方法
     *
     * @param data 要解密的密文
     * @return 解密之后的明文
     * @throws LxNonsupportException
     * @throws LxKeyException
     * @throws LxDecryptException
     */
    String decryptRSAWithUTF8(String data) throws LxNonsupportException, LxKeyException, LxDecryptException {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return new String(cipher.doFinal(new BASE64Decoder().decodeBuffer(data)), "UTF-8");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new LxNonsupportException(e.getMessage());
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            throw new LxNonsupportException(e.getMessage());
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            throw new LxKeyException(e.getMessage());
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
     * RSA 签名的方法
     *
     * @param content 要签名的内容
     * @return 签名之后再 base64 之后的内容
     * @throws LxNonsupportException
     * @throws LxSignatureException
     * @throws LxKeyException
     */
    String signWithUTF8(String content) throws LxNonsupportException, LxSignatureException, LxKeyException {
        try {
            Signature signature = Signature.getInstance("SHA1WithRSA");
            signature.initSign((PrivateKey) this.privateKey);
            signature.update(content.getBytes("UTF-8"));
            return new BASE64Encoder().encode(signature.sign());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new LxNonsupportException(e.getMessage());
        } catch (SignatureException e) {
            e.printStackTrace();
            throw new LxSignatureException(e.getMessage());
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            throw new LxKeyException(e.getMessage());
        } catch (UnsupportedEncodingException e) {
            throw new LxNonsupportException(e.getMessage());
        }
    }

    /**
     * RSA 签名验证
     *
     * @param content 明文内容
     * @param sign    要验证的密文
     * @return 返回签名验证是否通过
     * @throws LxNonsupportException
     * @throws LxKeyException
     * @throws LxVerifyException
     */
    Boolean verifyWithUTF8(String content, String sign) throws LxNonsupportException, LxKeyException, LxVerifyException {
        try {
            Signature signature = Signature.getInstance("SHA1WithRSA");

            signature.initVerify((PublicKey) this.publicKey);
            signature.update(content.getBytes("UTF-8"));

            return signature.verify(new BASE64Decoder().decodeBuffer(sign));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new LxNonsupportException(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            throw new LxVerifyException(e.getMessage());
        } catch (SignatureException e) {
            e.printStackTrace();
            throw new LxVerifyException(e.getMessage());
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            throw new LxKeyException(e.getMessage());
        }
    }

    /**
     * 生成 RSA 的 key
     *
     * @return
     * @throws LxNonsupportException
     */
    static Map<String, Key> generateKey() throws LxNonsupportException {

        KeyPairGenerator keyPairGen = null;
        try {
            keyPairGen = KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new LxNonsupportException(e.getMessage());
        }
        keyPairGen.initialize(KEY_LENGTH);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

        Map<String, Key> map = new HashMap<String, Key>();
        map.put("public", publicKey);
        map.put("private", privateKey);
        return map;

    }

    /**
     * 通过字符串生成 RSA 的公钥
     *
     * @param publicKey 公钥的字符串形式
     * @return RSA 的公钥
     * @throws LxKeyException
     * @throws LxNonsupportException
     */
    static PublicKey publicKey4String(String publicKey) throws LxKeyException, LxNonsupportException {
        try {
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(new BASE64Decoder().decodeBuffer(publicKey));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(keySpec);
        } catch (IOException e) {
            e.printStackTrace();
            throw new LxKeyException(e.getMessage());
        } catch (InvalidKeySpecException e) {
            throw new LxKeyException(e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            throw new LxNonsupportException(e.getMessage());
        }
    }

    /**
     * 根据字符串生成 RSA 的私钥
     *
     * @param privateKeyString RSA 字符串形式的私钥
     * @return RSA 的私钥
     * @throws LxKeyException
     * @throws LxNonsupportException
     */
    static PrivateKey privateKey4String(String privateKeyString) throws LxKeyException, LxNonsupportException {
        try {
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(new BASE64Decoder().decodeBuffer(privateKeyString));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePrivate(keySpec);
        } catch (IOException e) {
            e.printStackTrace();
            throw new LxKeyException(e.getMessage());
        } catch (InvalidKeySpecException e) {
            throw new LxKeyException(e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            throw new LxNonsupportException(e.getMessage());
        }
    }

    static String string4Key(Key key) {
        return new BASE64Encoder().encode(key.getEncoded());
    }
}
