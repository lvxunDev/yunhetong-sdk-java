package com.yunhetong.sdk.bean;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * <p>Title: LxContract</p>
 * <p>Description: 合同的实体类</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: www.yunhetong.com</p>
 *
 * @author wuyiping
 * @version 0.0.1
 */
public class LxContract{

    /**
     * 合同标题
     */
    protected String title;
    /**
     * 自定义合同编号
     */
    protected String defContractNo;

    /**
     * 合同模板id
     */
    protected String templateId;
    /**
     * 参数列表
     */
    protected HashMap<String, String> params;

    /**
     * 放个构造方法意思一下
     */
    public LxContract() {
    }

    /**
     * 带有合同相关参数和构造方法
     *
     * @param title         合同标题
     * @param defContractNo 自定义合同编号
     * @param templateId    合同模板id
     * @param params        合同参数列表
     */
    public LxContract(String title, String defContractNo, String templateId, LinkedHashMap<String, String> params) {
        this.title = title;
        this.defContractNo = defContractNo;
        this.templateId = templateId;
        this.params = params;
    }

    public String getTitle() {
        return title;
    }

    public LxContract setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDefContractNo() {
        return defContractNo;
    }

    public LxContract setDefContractNo(String defContractNo) {
        this.defContractNo = defContractNo;
        return this;
    }

    public String getTemplateId() {
        return templateId;
    }

    public LxContract setTemplateId(String templateId) {
        this.templateId = templateId;
        return this;
    }

    public HashMap<String, String> getParams() {
        return params;
    }

    public LxContract setParams(HashMap<String, String> params) {
        this.params = params;
        return this;
    }
}
