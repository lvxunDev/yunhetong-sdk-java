package com.yunhetong.sdk;


import com.yunhetong.sdk.bean.LxContract;
import com.yunhetong.sdk.bean.LxContractActor;
import com.yunhetong.sdk.bean.LxUser;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Date;

/**
 * <p>Title: LxMessageProvider</p>
 * <p>Description: 合同参与者类</p>
 * <p>合同参与者我们才不管甲方还是乙方呢，反正有几个参与者就传几个 Actor 就好了</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: www.yunhetong.com</p>
 *
 * @author wuyiping
 * @version 0.0.1
 */
final class LxMessageProvider {

    static String msgGetToken(String appid, LxUser user) {
        JSONObject json = new JSONObject();
        json.put("currentUser", json4User(appid, user));
        return json.toString();
    }

    static String msgGetTokenWithContract(String appid, LxUser user, final LxContract contract, final LxContractActor... actors) {
        JSONObject json = new JSONObject();
        json.put("currentUser", json4User(appid, user));
        json.put("contractFormVo", json4Contract(appid, contract, actors));
        return json.toString();
    }

    static String msgCreateContract(String appid, LxContract contract, LxContractActor... actors) {
        JSONObject json = new JSONObject();
        json.put("contractFormVo", json4Contract(appid, contract, actors));
        return json.toString();
    }

    static String msgInvalidContract(String appid, long contractId) {
        JSONObject json = new JSONObject();
        json.put("contractId", contractId);
        return json.toString();
    }

    static String msgQueryContracts(String appid, int pageNum, int pageSize) {
        JSONObject json = new JSONObject();
        json.put("flag", new Date().getTime());
        json.put("pageSize", pageSize < 10 ? 10 : pageSize);
        json.put("pageNum", pageNum < 1 ? 1 : pageNum);
        return json.toString();
    }

    /**
     * 将用户转成需要的 Json 对象
     *
     * @param appid appId
     * @param user  用户实体类
     * @return 返回需要的 Json 对象
     * @see LxUser
     */
    private static JSONObject json4User(String appid, LxUser user) {
        JSONObject json = new JSONObject();
        json.put("appId", appid);
        json.put("appUserId", user.getAppUserId());
        json.put("userType", user.getUserType().getValue());
        json.put("cellNum", user.getPhone());
        json.put("userName", user.getUserName());
        json.put("certifyType", user.getCertifyType().getValue());
        json.put("certifyNumber", user.getCertifyNumber());
        json.put("createSignature", user.getCreateSignature());
        return json;
    }

    /**
     * 将合同转需要的 Json 对象
     *
     * @param appid    appId
     * @param contract 合同是实体类
     * @param actors   参与者实体类数组
     * @return 返回符合格式的 Json 对象
     * @see LxContract
     * @see LxContractActor
     */
    private static JSONObject json4Contract(String appid, final LxContract contract, final LxContractActor[] actors) {

        JSONObject jsonVo = new JSONObject();

        jsonVo.put("appId", appid);
        jsonVo.put("title", contract.getTitle());
        jsonVo.put("defContractNo", contract.getDefContractNo());

        // -------------  占位符信息
        jsonVo.put("templateId", contract.getTemplateId());
        jsonVo.put("params", contract.getParams());

        // -------------  签约方方信息
        JSONArray array = new JSONArray();
        for (LxContractActor actor : actors) {
            array.put(json4Actor(appid, actor));
        }

        JSONObject json = new JSONObject();
        json.put("vo", jsonVo);
        json.put("attendUser", array);

        return json;
    }

    /**
     * 将 Actor 转成需要的 json 格式
     * autoSign 要转成相应的 0 和 1
     *
     * @param appid appId
     * @param actor 合同参与者的实体类
     * @return 返回封装好的 json
     * @see LxContractActor
     */
    private static JSONObject json4Actor(String appid, LxContractActor actor) {
        JSONObject json = new JSONObject();
        json.put("user", json4User(appid, actor));
        json.put("autoSign", (actor.getAutoSign() ? 1 : 0));
        json.put("locationName", actor.getLocationName());
        return json;
    }
}
