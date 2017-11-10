package com.yunhetong.sdk.util;

import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.util.*;

/**
 * <p>Title: LxHttpUtil</p>
 * <p>Description: 封装了一些Http请求 </p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: www.yunhetong.com</p>
 *
 * @author wuyiping
 * @version 0.0.1
 */
public class LxHttpUtil {

    private static String HOST = "http://sdk.yunhetong.com/sdk";
    private static String responseContentType = "";

    public static String getResponseContentType() {
        return responseContentType;
    }


    /**
     * 修改 HOST 的地址，使 SDK 成为调试模式
     * YHT 程序猿调试用，用户不需要使用这个方法
     * @param host
     * @return
     */
    public static boolean setDebug(String host){
        LxHttpUtil.HOST = host;
        return true;
    }
    public static String post(String url, Map<String, String> params) {
        String body = null;
        LxHttpUtil.responseContentType = "";
        CloseableHttpResponse response = sendRequest(HttpClients.createDefault(), postForm(url, params));
        body = paseResponse(response);
        Header contentTypeHeader = response.getFirstHeader("Content-Type");
        if (contentTypeHeader != null) {
            responseContentType = contentTypeHeader.getValue();
        }
        try {
            response.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return body;
    }

    /**
     * 下载的方法
     *
     * @param uri    下载的uri
     * @param params 下载的参数
     * @return 返回下下来的 byte 数组
     */
    public static byte[] download(String uri, Map<String, String> params) throws IOException {
        LxHttpUtil.responseContentType = "";
        CloseableHttpResponse response = sendRequest(HttpClients.createDefault(), postForm(HOST + uri, params));
        HttpEntity entity = response.getEntity();

        InputStream content = entity.getContent();
        byte[] bytes = LxIOUtils.toByteArray(content);
        Header contentTypeHeader = response.getFirstHeader("Content-Type");
        if (contentTypeHeader != null) {
            responseContentType = contentTypeHeader.getValue();
        }
        response.close();
        return bytes;
    }

    /**
     * 下载的方法
     *
     * @param uri    下载的 uri 链接
     * @param appid  第三方应用的 AppId
     * @param secret 加密后的密文
     * @return 返回下载下来的 byte 数组
     * @throws IOException
     */
    public static byte[] download(String uri, String appid, String secret) throws IOException {
        Map<String, String> params = new HashMap<String, String>();
        params.put("appid", appid);
        params.put("secret", secret);
        return download(uri, params);
    }

    public static String post(String url, String appid, String secret) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("appid", appid);
        params.put("secret", secret);
        return post(HOST + url, params);
    }


    public static String get(String url) {
        String body = null;

        HttpGet get = new HttpGet(HOST + url);
        get.setConfig(requestConfig());

        CloseableHttpResponse response = sendRequest(HttpClients.createDefault(), get);
        body = paseResponse(response);
        try {
            response.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return body;
    }

    private static CloseableHttpResponse sendRequest(CloseableHttpClient httpclient, HttpUriRequest httpost) {
        CloseableHttpResponse response = null;
        httpost.addHeader("sdkType", "javaSDK");
        try {
            response = httpclient.execute(httpost);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    private static HttpPost postForm(String url, Map<String, String> params) {

        HttpPost httpost = new HttpPost(url);
        httpost.setConfig(requestConfig());

        List<NameValuePair> nvps = new ArrayList<NameValuePair>();

        Iterator iter = params.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            nvps.add(new BasicNameValuePair((String) entry.getKey(), (String) entry.getValue()));
        }

        try {
            httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return httpost;
    }

    private static RequestConfig requestConfig() {
        return RequestConfig.custom().setSocketTimeout(20000).setConnectTimeout(20000).build();
    }


    private static String paseResponse(HttpResponse response) {

        if (response == null) {
            return null;
        }

        HttpEntity entity = response.getEntity();

        String body = null;
        try {
            body = EntityUtils.toString(entity);
            EntityUtils.consume(entity);
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return body;
    }
}
