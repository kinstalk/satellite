package com.kinstalk.satellite.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 */
public class HttpUtils {

    public static String getUrl(String url) {

        // (1) 创建HttpGet实例
        HttpGet get = new HttpGet(url);

        // (2) 使用HttpClient发送get请求，获得返回结果HttpResponse
        HttpClient http = new DefaultHttpClient();
        HttpResponse response = null;
        try {
            // (3) 读取返回结果
            response = http.execute(get);
            //临时存储字符串。
            StringBuffer buffer = new StringBuffer();
            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                InputStream in = null;
                try {
                    in = entity.getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        buffer.append(line);
                    }

                } finally {
                    //关闭输入流
                    if (in != null)
                        in.close();
                }
                return buffer.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //释放连接。http://www.tuicool.com/articles/VJjQV3
            try {
                get.abort();
            } catch (Exception e) {

            }
            try {
                // 释放连接
                get.releaseConnection();
            } catch (Exception e) {

            }
            if (response != null) {
                try {
                    EntityUtils.consumeQuietly(response.getEntity());
                } catch (Exception e) {

                }
            }
        }
        return "";
    }
}
