package com.xueqiu.fund.selenium.service;

import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.util.TextUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedHashMap;
import java.util.Map;

public class FundHeaders {
    private static Map<String,String> sepHeaders = new LinkedHashMap<String, String>();
    private static Map<String,String> releaseHeaders;

    public static Map<String,String> getInstanceHeaders() {
        sepHeaders.put("dj-client","iOS");
        sepHeaders.put("dj-version","1.0");
        sepHeaders.put("dj-device-id:","test");
        sepHeaders.put("Authorization","OAuth2 "+ "foreverValidToken");
        sepHeaders.put("Accept","*/*");
        if ( releaseHeaders == null)
        {
            releaseHeaders = new LinkedHashMap<String, String>();
            long currentTime = System.currentTimeMillis();
            String djTimestamp = currentTime + "";
            String djSignature = getReqEncrypt(djTimestamp);

            releaseHeaders.put("Host","fund.xueqiu.com");
            releaseHeaders.put("dj-device-id","test");
            releaseHeaders.put("Proxy-Connection","keep-alive");
            releaseHeaders.put("dj-version","1.5");
            releaseHeaders.put("Accept","*/*");
            releaseHeaders.put("Accept-Language","zh-cn");
            releaseHeaders.put("Accept-Encoding","gzip, deflate");
            releaseHeaders.put("dj-client","iOS");
            releaseHeaders.put("dj-timestamp",djTimestamp);
            releaseHeaders.put("User-Agent","Fund iPhone 1.5");
            releaseHeaders.put("Connection","keep-alive");
            releaseHeaders.put("dj-signature",djSignature);
            //releaseHeaders.put("Cookie","u=383889121");
            //releaseHeaders.put("Authorization","OAuth2 "+ "foreverValidToken");
        }

        return sepHeaders;
    }

    public static void setHeaders(HttpRequestBase httpRequestBase){
        Map<String,String> headers = getInstanceHeaders();

        for (Map.Entry<String, String> header : headers.entrySet()) {
            httpRequestBase.setHeader(header.getKey(),header.getValue());
        }
    }

    private static String md5(String s) {
        char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
        try {
            byte[] btInput = s.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String sha1(String data) {
        try {
            MessageDigest digest = MessageDigest
                    .getInstance("SHA-1");
            digest.update(data.getBytes());
            byte messageDigest[] = digest.digest();
            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            // 字节数组转换为 十六进制 数
            for (int i = 0; i < messageDigest.length; i++) {
                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(shaHex);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    private static String getReqEncrypt(String time) {
        if (TextUtils.isEmpty(time)) {
            return "";
        }
        try {
            String info = "XQDJ_SINCE_2016" + md5(time);
            return sha1(info);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void main(String[] args){
        System.out.println("密码是:" + getReqEncrypt("1466410012873"));
    }




}
