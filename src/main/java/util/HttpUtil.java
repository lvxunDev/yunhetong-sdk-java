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
 * Created by wuyiping on 16/3/2.
 */
public class HttpUtil {

    private static final Logger log = Logger.getLogger(String.valueOf(HttpUtil.class));

    private static String HOST = "http://localhost:8080";

    public static Map post(String url, Map<String, String> params) throws Exception{

        String body = null;
        int count = 0;
        CloseableHttpResponse __response = sendRequest(initHttp(), postForm(url, params));
        while (true){
            if(__response!=null && __response.getStatusLine().getStatusCode()==200 ){
                break;
            }
            if(count==2){
                break;
            }
            count++;
            __response = sendRequest(initHttp(), postForm(url, params));
        }
        if(__response!=null && __response.getStatusLine().getStatusCode()==200){
            body = paseResponse(__response);
        }
        try {
            if(__response!=null){
                __response.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Map map = new HashMap();
        map.put("body",body);
        map.put("code",__response.getStatusLine().getStatusCode());

        return map;
    }

    private static CloseableHttpClient initHttp(){
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(10000)
                .setConnectionRequestTimeout(10000)
                .setConnectTimeout(10000)
                .build();
        CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(requestConfig).build();
        return httpClient;
    }

    public static String get(String url) throws Exception{
        String body = null;

        CloseableHttpResponse __response = sendRequest(HttpClients.createDefault(), new HttpGet(url));
        body = paseResponse(__response);
        try {
            __response.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return body;
    }

    private static CloseableHttpResponse sendRequest(CloseableHttpClient httpclient, HttpUriRequest httpost) throws Exception{
        CloseableHttpResponse response = null;
        response = httpclient.execute(httpost);
        return response;
    }

    private static HttpPost postForm(String url, Map<String, String> params) {

        HttpPost httpost = new HttpPost(url);
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();

        Iterator iter = params.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            nvps.add(new BasicNameValuePair((String) entry.getKey(), (String)entry.getValue()));
        }

        try {
            httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return httpost;
    }


    private static String paseResponse(HttpResponse response) {

        String body = null;
        if(response!=null){
            HttpEntity __entity = response.getEntity();
            try {
                body = EntityUtils.toString(__entity);
                EntityUtils.consume(__entity);
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return body;
    }

//    public static void main(String[] args){
//        Map map = new HashMap();
//        map.put("appid","23432352345");
//        map.put("secret","dsafdasfdasfdasfd");
//        System.out.println(HttpUtil.post("http://localhost:8080/third/listContract",map));
//    }
}
