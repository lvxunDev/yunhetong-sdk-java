package secret;

import exception.*;
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

    void initPublicKey4Stream(InputStream stream) throws LxKeyException, LxNonsupportException {
        try {
            publicKey = LxRSAHandler.publicKey4String(readStream(stream));
        } catch (IOException e) {
            e.printStackTrace();
            throw new LxKeyException(e.getMessage());
        }
    }

    void initPrivateKey4Stream(InputStream stream) throws LxKeyException, LxNonsupportException {
        try {
            privateKey = LxRSAHandler.privateKey4String(readStream(stream));
        } catch (IOException e) {
            e.printStackTrace();
            throw new LxKeyException(e.getMessage());
        }
    }

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
        RSAPublicKey publickey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privatekey = (RSAPrivateKey) keyPair.getPrivate();

        Map<String, Key> map = new HashMap<String, Key>();
        map.put("public", publickey);
        map.put("private", privatekey);
        return map;

    }

    static PublicKey publicKey4String(String __publicKey) throws LxKeyException, LxNonsupportException {
        try {
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(new BASE64Decoder().decodeBuffer(__publicKey));
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
