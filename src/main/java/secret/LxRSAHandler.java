package secret;

import com.yunhetong.sdk.util.exception.*;
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
 * Created by wuyiping on 16/2/29.
 */
final class LxRSAHandler {

    Key publicKey;
    Key privateKey;

    private static final int KEY_LENGTH = 1024;

    void initPublicKey4Stream(InputStream __stream) throws LxKeyException, LxUnsupportException {
        try {
            publicKey = LxRSAHandler.publicKey4String(readStream(__stream));
        } catch (IOException e) {
            e.printStackTrace();
            throw new LxKeyException(e.getMessage());
        }
    }

    void initPrivateKey4Stream(InputStream __stream) throws LxKeyException, LxUnsupportException {
        try {
            privateKey = LxRSAHandler.privateKey4String(readStream(__stream));
        } catch (IOException e) {
            e.printStackTrace();
            throw new LxKeyException(e.getMessage());
        }
    }

    private static String readStream(InputStream __stream) throws IOException {
        InputStreamReader __read = null;
        BufferedReader __bufferedReader = null;
        StringBuilder __sb = null;
        try {
            __read = new InputStreamReader(__stream);
            __bufferedReader = new BufferedReader(__read);
            __sb = new StringBuilder();
            String __readLine;
            while ((__readLine = __bufferedReader.readLine()) != null) {
                __sb.append(__readLine);
            }
        } finally {
            if (__bufferedReader != null) __bufferedReader.close();
            if (__read != null) __read.close();
        }
        return __sb == null ? null : __sb.toString();
    }

    String encryptRSAWithUTF8(String __data) throws LxUnsupportException, LxKeyException, LxEncrypteException {
        try {
            Cipher __cipher = Cipher.getInstance("RSA");
            __cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return new BASE64Encoder().encode(__cipher.doFinal(__data.getBytes("UTF-8")));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new LxUnsupportException(e.getMessage());
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            throw new LxUnsupportException(e.getMessage());
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            throw new LxKeyException(e.getMessage());
        } catch (BadPaddingException e) {
            e.printStackTrace();
            throw new LxEncrypteException(e.getMessage());
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
            throw new LxEncrypteException(e.getMessage());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new LxUnsupportException(e.getMessage());
        }
    }

    String decryptRSAWithUTF8(String __data) throws LxUnsupportException, LxKeyException, LxDecryptException {
        try {
            Cipher __cipher = Cipher.getInstance("RSA");
            __cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return new String(__cipher.doFinal(new BASE64Decoder().decodeBuffer(__data)), "UTF-8");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new LxUnsupportException(e.getMessage());
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            throw new LxUnsupportException(e.getMessage());
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

    String signWithUTF8(String __content) throws LxUnsupportException, LxSignatureException, LxKeyException {
        try {
            Signature __signature = Signature.getInstance("SHA1WithRSA");
            __signature.initSign((PrivateKey) this.privateKey);
            __signature.update(__content.getBytes("UTF-8"));
            return new BASE64Encoder().encode(__signature.sign());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new LxUnsupportException(e.getMessage());
        } catch (SignatureException e) {
            e.printStackTrace();
            throw new LxSignatureException(e.getMessage());
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            throw new LxKeyException(e.getMessage());
        } catch (UnsupportedEncodingException e) {
            throw new LxUnsupportException(e.getMessage());
        }
    }

    Boolean verifyWithUTF8(String __content, String __sign) throws LxUnsupportException, LxKeyException, LxVerifyException {
        try {
            Signature __signature = Signature.getInstance("SHA1WithRSA");

            __signature.initVerify((PublicKey) this.publicKey);
            __signature.update(__content.getBytes("UTF-8"));

            return __signature.verify(new BASE64Decoder().decodeBuffer(__sign));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new LxUnsupportException(e.getMessage());
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

    static Map<String, Key> generateKey() throws LxUnsupportException {

        KeyPairGenerator keyPairGen = null;
        try {
            keyPairGen = KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new LxUnsupportException(e.getMessage());
        }
        keyPairGen.initialize(KEY_LENGTH);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPublicKey __publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey __privateKey = (RSAPrivateKey) keyPair.getPrivate();

        Map<String, Key> __map = new HashMap<String, Key>();
        __map.put("public", __publicKey);
        __map.put("private", __privateKey);
        return __map;

    }

    static PublicKey publicKey4String(String __publicKey) throws LxKeyException, LxUnsupportException {
        try {
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(new BASE64Decoder().decodeBuffer(__publicKey));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = keyFactory.generatePublic(keySpec);
            return publicKey;
        } catch (IOException e) {
            e.printStackTrace();
            throw new LxKeyException(e.getMessage());
        } catch (InvalidKeySpecException e) {
            throw new LxKeyException(e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            throw new LxUnsupportException(e.getMessage());
        }
    }

    static PrivateKey privateKey4String(String __privateKey) throws LxKeyException, LxUnsupportException {
        try {
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(new BASE64Decoder().decodeBuffer(__privateKey));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
            return privateKey;
        } catch (IOException e) {
            e.printStackTrace();
            throw new LxKeyException(e.getMessage());
        } catch (InvalidKeySpecException e) {
            throw new LxKeyException(e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            throw new LxUnsupportException(e.getMessage());
        }


    }

    static String string4Key(Key __key) {
        return new BASE64Encoder().encode(__key.getEncoded());
    }
}
