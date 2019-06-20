package com.yxjh.api.encrypted;

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
    public static void main(String args[]){
        String password="Admin123456";
        System.out.println(encryptionByMD5(password));
    }
}
