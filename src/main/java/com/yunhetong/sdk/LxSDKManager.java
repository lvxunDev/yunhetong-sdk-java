package com.yunhetong.sdk;


import com.yunhetong.sdk.bean.LxContract;
import com.yunhetong.sdk.bean.LxContractActor;
import com.yunhetong.sdk.bean.LxUser;
import com.yunhetong.sdk.exception.*;
import com.yunhetong.sdk.secret.LxSecretManager;
import com.yunhetong.sdk.util.LxHttpUtil;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * <p>Title: LxSDKManager</p>
 * <p>Description: SDK 管理类</p>
 * <p>用户用SDK时主要的类，基本上所有的方法都在这里了</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: www.yunhetong.com</p>
 *
 * @author wuyiping
 * @version 0.0.1
 */
public final class LxSDKManager {

    private String appid;
    private LxSecretManager secretManager;

    /**
     * 构造方法
     *
     * @param appid    第三方应用平台的 appId
     * @param pubFile  云合同的公钥地址
     * @param privFile 第三方应用平台的私钥地址
     * @throws IOException
     * @throws LxKeyException
     * @throws LxNonsupportException
     */
    public LxSDKManager(String appid, File pubFile, File privFile) throws IOException, LxKeyException, LxNonsupportException {
        this.appid = appid;
        secretManager = new LxSecretManager(new FileInputStream(pubFile), new FileInputStream(privFile));
    }

    /**
     * 构造方法
     *
     * @param appid      第三方平台的 appId
     * @param pubStream  云合同的公钥文件
     * @param privStream 第三方应用平台的私钥文件
     * @throws IOException
     * @throws LxKeyException
     * @throws LxNonsupportException
     */
    public LxSDKManager(String appid, InputStream pubStream, InputStream privStream) throws IOException, LxKeyException, LxNonsupportException {
        this.appid = appid;
        secretManager = new LxSecretManager(pubStream, privStream);
    }

    /**
     * 加密方法
     *
     * @param json 要加密的 json 字符串
     * @return 返回加密后的字符串
     * @throws LxEncryptException
     * @throws LxKeyException
     * @throws LxNonsupportException
     * @throws LxSignatureException
     */
    public String encryptWithUTF8(String json) throws LxEncryptException, LxKeyException, LxNonsupportException, LxSignatureException {
        return secretManager.encryptWithUTF8(json);
    }

    /**
     * 解密方法
     *
     * @param json 要解密 json 字符串
     * @return 返回解密好的字符串
     * @throws LxKeyException
     * @throws LxNonsupportException
     * @throws LxDecryptException
     * @throws LxVerifyException
     */
    public String decryptWithUTF8(String json) throws LxKeyException, LxNonsupportException, LxDecryptException, LxVerifyException {
        return secretManager.decryptWithUTF8(json);
    }


    /**
     * 获取用户 Token 的方法
     *
     * @param user 用户实体类
     * @return 返回解密后的云合同服务端返回
     * @throws IOException
     * @throws LxEncryptException
     * @throws LxKeyException
     * @throws LxNonsupportException
     * @throws LxDecryptException
     * @throws LxSignatureException
     * @throws LxVerifyException
     */
    public String getToken(LxUser user) throws IOException, LxEncryptException, LxKeyException, LxNonsupportException, LxDecryptException, LxSignatureException, LxVerifyException {
        String source = LxMessageProvider.msgGetToken(this.appid, user);
        String secret = secretManager.encryptWithUTF8(source);
        String response = LxHttpUtil.post("/third/tokenWithUser", this.appid, secret);
        return secretManager.decryptWithUTF8(response);
    }

    /**
     * 更新用户信息
     *
     * @param user 要更新的用户信息，以 appUserId 为唯一标识，只能更新用户的电话号码和用户名
     * @return
     * @throws LxEncryptException
     * @throws LxKeyException
     * @throws LxNonsupportException
     * @throws LxDecryptException
     * @throws LxSignatureException
     * @throws LxVerifyException
     */
    public String updateUserInfo(LxUser user) throws LxEncryptException, LxKeyException, LxNonsupportException, LxDecryptException, LxSignatureException, LxVerifyException {
        String source = LxMessageProvider.msgGetToken(this.appid, user);
        String secret = secretManager.encryptWithUTF8(source);
        String response = LxHttpUtil.post("/third/userUpdate", this.appid, secret);
        return secretManager.decryptWithUTF8(response);
    }

    /**
     * 获取用户 Token 并且创建合同的方法
     *
     * @param user     用户实体类
     * @param contract 合同实体类
     * @param actors   合同参与者实体类
     * @return 返回解密后的云合同服务端返回
     * @throws IOException
     * @throws LxEncryptException
     * @throws LxKeyException
     * @throws LxNonsupportException
     * @throws LxDecryptException
     * @throws LxSignatureException
     * @throws LxVerifyException
     */
    public String getTokenWithContract(LxUser user, final LxContract contract, final LxContractActor... actors) throws IOException, LxEncryptException, LxKeyException, LxNonsupportException, LxDecryptException, LxSignatureException, LxVerifyException {
        String source = LxMessageProvider.msgGetTokenWithContract(this.appid, user, contract, actors);
        String secret = secretManager.encryptWithUTF8(source);
        String response = LxHttpUtil.post("/third/tokenWithContract", this.appid, secret);
        return secretManager.decryptWithUTF8(response);
    }

    /**
     * 创建合同的方法
     *
     * @param contract 合同实体类
     * @param actors   参与方实体类
     * @return 返回解密后的云合同服务端返回
     * @throws LxEncryptException
     * @throws LxKeyException
     * @throws LxNonsupportException
     * @throws LxDecryptException
     * @throws LxSignatureException
     * @throws LxVerifyException
     */
    public String createContract(LxContract contract, LxContractActor... actors) throws LxEncryptException, LxKeyException, LxNonsupportException, LxDecryptException, LxSignatureException, LxVerifyException {
        String source = LxMessageProvider.msgCreateContract(this.appid, contract, actors);
        String secret = secretManager.encryptWithUTF8(source);
        String response = LxHttpUtil.post("/third/autoContract", this.appid, secret);
        return secretManager.decryptWithUTF8(response);
    }

    /**
     * 下载合同的方法
     *
     * @param contractId 要下载的合同的合同 Id
     * @return 失败的话返回错误的 json 字符串，成功的话返回合同的 zip 流
     * @throws LxEncryptException
     * @throws LxKeyException
     * @throws LxNonsupportException
     * @throws LxDecryptException
     * @throws LxSignatureException
     * @throws LxVerifyException
     */
    public Map<String, Object> downloadContract(long contractId) throws LxEncryptException, LxKeyException, LxNonsupportException, LxDecryptException, LxSignatureException, LxVerifyException, IOException {
        JSONObject json = new JSONObject();
        json.put("contractId", contractId);
        json.put("timestamp", new Date().getTime());
        String source = json.toString();
        String secret = secretManager.encryptWithUTF8(source);
        byte[] response = LxHttpUtil.download("/third/download", this.appid, secret);
        Map<String, Object> retMap = new HashMap<String, Object>(2);
        retMap.put("success", LxHttpUtil.getResponseContentType().equals("application/octet-stream;charset=UTF-8"));
        retMap.put("body", response);
        return retMap;
    }

    /**
     * 作废合同的接口
     *
     * @param contractId 要作废的合同 id
     * @return invalidContract
     * @throws LxEncryptException
     * @throws LxKeyException
     * @throws LxNonsupportException
     * @throws LxDecryptException
     * @throws LxSignatureException
     * @throws LxVerifyException
     */
    public String invalidContract(long contractId) throws LxEncryptException, LxKeyException, LxNonsupportException, LxDecryptException, LxSignatureException, LxVerifyException {
        String secret = secretManager.encryptWithUTF8(LxMessageProvider.msgInvalidContract(this.appid, contractId));
        String response = LxHttpUtil.post("/third/invalidContract", this.appid, secret);
        return secretManager.decryptWithUTF8(response);
    }

    /**
     * 获取最后一次发送的消息
     * 由于消息回调的方法本地不好调试，我们添加了一个获取最后一次消息的接口，方便本地调试
     * 使用方法：在完成合同操作之后，调用这个方法去获取我们服务端发的消息，然后再处理直接拿到消息模拟消息回调的过程。
     *
     * @return invalidContract
     * @throws LxEncryptException
     * @throws LxKeyException
     * @throws LxNonsupportException
     * @throws LxDecryptException
     * @throws LxSignatureException
     * @throws LxVerifyException
     */
    public String getLastNotice() throws LxEncryptException, LxKeyException, LxNonsupportException, LxDecryptException, LxSignatureException, LxVerifyException {
        String response = LxHttpUtil.get("/third/getLastNotice?appid=" + this.appid);
        return secretManager.decryptWithUTF8(response);
    }

    /**
     * 合同查询的接口
     *
     * @param pageNum  页数
     * @param pageSize 每页的合同数量
     * @return 直接和房东签个几年合同
     * @throws LxEncryptException
     * @throws LxKeyException
     * @throws LxNonsupportException
     * @throws LxDecryptException
     * @throws LxSignatureException
     * @throws LxVerifyException
     */
    public String queryContracts(int pageNum, int pageSize) throws LxEncryptException, LxKeyException, LxNonsupportException, LxDecryptException, LxSignatureException, LxVerifyException {
        String secret = secretManager.encryptWithUTF8(LxMessageProvider.msgQueryContracts(this.appid, pageNum, pageSize));
        String response = LxHttpUtil.post("/third/listContract", this.appid, secret);
        return secretManager.decryptWithUTF8(response);
    }

    /**
     * 对云合同返回的消息进行签名的接口
     *
     * @param content 要签名的内容
     * @return 返回签名后的内容
     * @throws LxNonsupportException
     * @throws LxSignatureException
     * @throws LxKeyException
     */
    public String signWithUTF8(String content) throws LxNonsupportException, LxSignatureException, LxKeyException {
        return this.secretManager.signWithUTF8(content);
    }

    /**
     * 验证云合同签名的接口
     *
     * @param content 内容
     * @param sign    签名之后的数据
     * @return 返回签名是否正确
     * @throws LxVerifyException
     * @throws LxKeyException
     * @throws LxNonsupportException
     */
    public boolean verifyWithUTF8(String content, String sign) throws LxVerifyException, LxKeyException, LxNonsupportException {
        return this.secretManager.verifyWithUTF8(content, sign);
    }
}
