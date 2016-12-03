package util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.logging.Logger;

/**
 * <p>Title: HttpUtil</p>
 * <p>Description: 封装了一些Http请求 </p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: www.yunhetong.com</p>
 *
 * @author wuyiping
 * @version 0.0.1
 */
public class HttpUtil {

    private static final Logger log = Logger.getLogger(String.valueOf(HttpUtil.class));

    public static Map post(String url, Map<String, String> params) throws Exception {

        String body = null;
        int count = 0;
        CloseableHttpResponse response = sendRequest(initHttp(), postForm(url, params));
        while (true) {
            if (response != null && response.getStatusLine().getStatusCode() == 200) {
                break;
            }
            if (count == 2) {
                break;
            }
            count++;
            response = sendRequest(initHttp(), postForm(url, params));
        }
        if (response != null && response.getStatusLine().getStatusCode() == 200) {
            body = paseResponse(response);
        }
        try {
            if (response != null) {
                response.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Map map = new HashMap();
        map.put("body", body);
        map.put("code", response.getStatusLine().getStatusCode());

        return map;
    }

    private static CloseableHttpClient initHttp() {
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(10000)
                .setConnectionRequestTimeout(10000)
                .setConnectTimeout(10000)
                .build();
        CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(requestConfig).build();
        return httpClient;
    }

    public static String get(String url) throws Exception {
        String body = null;

        CloseableHttpResponse response = sendRequest(HttpClients.createDefault(), new HttpGet(url));
        body = paseResponse(response);
        try {
            response.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return body;
    }

    private static CloseableHttpResponse sendRequest(CloseableHttpClient httpclient, HttpUriRequest httpost) throws Exception {
        CloseableHttpResponse response = null;
        httpost.addHeader("sdkType","javaSDK");
        response = httpclient.execute(httpost);
        return response;
    }

    private static HttpPost postForm(String url, Map<String, String> params) {

        HttpPost httpPost = new HttpPost(url);
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();

        for (Object o : params.entrySet()) {
            Map.Entry entry = (Map.Entry) o;
            nvps.add(new BasicNameValuePair((String) entry.getKey(), (String) entry.getValue()));
        }

        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return httpPost;
    }


    private static String paseResponse(HttpResponse response) {

        String body = null;
        if (response != null) {
            HttpEntity entity = response.getEntity();
            try {
                body = EntityUtils.toString(entity);
                EntityUtils.consume(entity);
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return body;
    }

}
