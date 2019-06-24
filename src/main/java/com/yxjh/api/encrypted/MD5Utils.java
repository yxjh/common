package com.yxjh.api.encrypted;

import com.yxjh.api.file.FileUtils;
import org.apache.commons.codec.binary.Base64;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;

/**
 * @program: common
 * @author: xuWei
 * @create: 2019/06/18
 * @description: 加密MD5相关公共方法
 */
public class MD5Utils {


    /**
     * md5--16进制加密
     * @param data
     * @return
     */
    public static  String encryptionByMD5(String data){
        StringBuilder sb=null;
        try {
            MessageDigest md=MessageDigest.getInstance("md5");
            sb=new StringBuilder();
            byte[]bytes=md.digest(data.getBytes());
            for(byte b:bytes){
                int number =b&0xff;
                String hex=Integer.toHexString(number);
                if(hex.length()==1){
                    sb.append("0"+hex);
                }else {
                    sb.append(hex);
                }
            }
            return sb.toString();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据网络路径生成base64串
     * @param imageUrl
     * @return
     */
    public static String getURLImage(String imageUrl){
        InputStream inStream = null;
        String result=null;
        try {
            //new一个URL对象
            String saa = imageUrl.substring(imageUrl.lastIndexOf("/")+1);
            if (saa.contains(".")) {
                imageUrl = imageUrl.substring(0, imageUrl.lastIndexOf("/")) + "/" + URLEncoder.encode(saa, "utf-8");
            }
            URL url = new URL(imageUrl);
            //打开链接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //设置请求方式为"GET"
            conn.setRequestMethod("GET");
            //超时响应时间为5秒
            conn.setConnectTimeout(5 * 1000);
            //通过输入流获取图片数据
            inStream = conn.getInputStream();
            //得到图片的二进制数据，以二进制封装得到数据，具有通用性
            byte[] data = FileUtils.readInputStream(inStream);
            result = Base64.encodeBase64String(data);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(inStream!=null){
                try {
                    inStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
}
