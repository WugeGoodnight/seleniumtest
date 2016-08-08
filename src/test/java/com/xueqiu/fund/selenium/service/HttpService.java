package com.xueqiu.fund.selenium.service;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpService {
    public enum HttpMethod{
        get,post,put,delete
    }

    private static String privateHost ;

    public static String getInstanceHost()
    {
        if (privateHost == null)
        {
            privateHost = "http://10.11.202.141:8081";
        }

        return privateHost;
    }

    public static void setInstanceHost(String newHost)
    {
        privateHost = newHost;
    }

//    private static String sepUriPre = "http://10.11.202.1:8081";
//    private static String releaseUriPre = "https://182.92.251.113";

    public static String getStrResBody(String uri, Map<String,String> formparams,HttpMethod httpMethod) {
        //String sepUriPre = "http://10.11.202.1:8081";
        String releaseUriPre = getInstanceHost();

        String url = releaseUriPre + uri;

        if ( httpMethod == HttpMethod.get )
        {
            HttpGet httpGet = new HttpGet(url);
            return executeReq(httpGet);
        }
        else if ( httpMethod == HttpMethod.post )
        {
            HttpPost httpPost = new HttpPost(url);

            if (formparams == null)
            {
                return executeReq(httpPost);
            }
            else
            {
                List<NameValuePair> currentFormparams = new ArrayList<NameValuePair>();
                for (Map.Entry<String, String> formparam : formparams.entrySet()) {
                    currentFormparams.add(new BasicNameValuePair(formparam.getKey(), formparam.getValue()));
                }

                UrlEncodedFormEntity uefEntity;
                try {
                    uefEntity = new UrlEncodedFormEntity(currentFormparams, "UTF-8");
                    httpPost.setEntity(uefEntity);
                }catch (Exception e){
                    e.printStackTrace();
                }
                return executeReq(httpPost);
            }

        }
        else if ( httpMethod == HttpMethod.put )
        {
            HttpPut httpPut = new HttpPut(url);
            if (formparams == null)
            {
                return executeReq(httpPut);
            }

            List<NameValuePair> currentFormparams = new ArrayList<NameValuePair>();
            for (Map.Entry<String, String> formparam : formparams.entrySet()) {
                currentFormparams.add(new BasicNameValuePair(formparam.getKey(), formparam.getValue()));
            }

            UrlEncodedFormEntity uefEntity;
            try {
                uefEntity = new UrlEncodedFormEntity(currentFormparams, "UTF-8");
                httpPut.setEntity(uefEntity);
            }catch (Exception e){
                e.printStackTrace();
            }

            return executeReq(httpPut);
        }
        else if (httpMethod == HttpMethod.delete)
        {
            HttpDelete httpDelete = new HttpDelete(url);
            return executeReq(httpDelete);
        }
        else
        {
            HttpGet httpGet = new HttpGet(url);
            return executeReq(httpGet);
        }


    }

    public static String getStrResBody(String uri,HttpMethod httpMethod) {
        return getStrResBody(uri,null,httpMethod);
    }

    public static JSONObject getJsonResBody(String uri, Map<String,String> formparams,HttpMethod httpMethod) {
        String strRes = getStrResBody(uri,formparams,httpMethod);
        return new JSONObject(strRes);
    }

    public static JSONObject getJsonResBody(String uri,HttpMethod httpMethod) {
        String strRes = getStrResBody(uri,httpMethod);
        return new JSONObject(strRes);
    }

    private static String executeReq(HttpRequestBase httpRequestBase) {
        String strRes = "";

        //设置头
        FundHeaders.setHeaders(httpRequestBase);

        CloseableHttpClient httpclient = HttpClients.custom().
                setSSLHostnameVerifier(new NoopHostnameVerifier()).build();

//        HttpHost proxy = new HttpHost("127.0.0.1",7777,"http");
//        RequestConfig config = RequestConfig.custom().setProxy(proxy).build();
//        httpRequestBase.setConfig(config);

        System.out.println("请求的header是:");
        Header[] headers = httpRequestBase.getAllHeaders();
        for (Header h :headers){

            System.out.println(h.getName() + "------ " + h.getValue());

        }

        try{
            //uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
            //httpPost.setEntity(uefEntity);
            System.out.println("请求的URL是:" +
                    "\n httpLine :" + httpRequestBase.getRequestLine() );
            CloseableHttpResponse response = httpclient.execute(httpRequestBase);

            try {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    InputStream in = entity.getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    strRes = reader.readLine();
                    System.out.println("请求的结果是:" + strRes);
                }
            } finally {
                response.close();
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return strRes;
    }


}
