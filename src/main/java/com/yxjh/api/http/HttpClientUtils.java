package com.yxjh.api.http;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @program: common
 * @author: xuWei
 * @create: 2019/06/17
 * @description: httpclient公共方法
 */
public class HttpClientUtils {


    /**
     * post请求方式
     * @param url   访问路径
     * @param json  body参数
     * @param token token
     * @return
     */
    public static String httpPostBody(String url,String json,String token){
        String result=null;
        try {
            HttpClient client=new DefaultHttpClient();
            client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,2000);
            client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,2000);
            HttpPost post=new HttpPost(url);
            if(token!=null){
                post.setHeader("Authorization",token);
            }
            if(json!=null){
                post.setHeader("Content-Type","application/json");
                //构建消息实体
                StringEntity s=new StringEntity(json, Charset.forName("UTF-8"));
                s.setContentEncoding("UTF-8");
                post.setEntity(s);
            }
            HttpResponse response=client.execute(post);
            HttpEntity entity=response.getEntity();
            result= EntityUtils.toString(entity,"utf-8");
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }


    /**
     * get请求方式
     * @param url     网络路径
     * @param parMap  参数Map
     * @param token   token
     * @return
     */
    public static String httpGet(String url,Map<String,String> parMap,String token){
        // 获取连接客户端工具
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String entityStr = null;
        CloseableHttpResponse response = null;
        try {
            URIBuilder uriBuilder = new URIBuilder(url);
            /** 第二种添加参数的形式 */
            List<NameValuePair> list = new LinkedList<>();
            for (Map.Entry<String, String> entry : parMap.entrySet()) {
                BasicNameValuePair param = new BasicNameValuePair(entry.getKey(), entry.getValue());
                list.add(param);
            }
            uriBuilder.setParameters(list);
            // 根据带参数的URI对象构建GET请求对象
            HttpGet httpGet = new HttpGet(uriBuilder.build());
            /*
             * 添加请求头信息
             */
            // 浏览器表示
            httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.7.6)");
            // 传输的类型
            httpGet.addHeader("Content-Type", "application/x-www-form-urlencoded");

            if(token!=null){
                httpGet.addHeader("Authorization",token);
            }
            // 执行请求
            response = httpClient.execute(httpGet);
            // 获得响应的实体对象
            HttpEntity entity = response.getEntity();
            // 使用Apache提供的工具类进行转换成字符串
            entityStr = EntityUtils.toString(entity, "UTF-8");
        }  catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 释放连接
            if (null != response) {
                try {
                    response.close();
                    httpClient.close();
                } catch (Exception e) {
                    System.err.println("释放连接出错");
                    e.printStackTrace();
                }
            }
        }
        return  entityStr;
    }


}
