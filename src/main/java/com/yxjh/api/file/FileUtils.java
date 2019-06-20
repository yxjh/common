package com.yxjh.api.file;

import org.apache.commons.codec.binary.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * @program: common
 * @author: xuWei
 * @create: 2019/06/17
 * @description: 文件相关的公共方法
 */
public class FileUtils {

    /**
     * 递归删除文件下的所有文件
     * @param file
     */
    public static void deleteFile(File file){
        if (file.isFile()){//判断是否为文件，是，则删除
            file.delete();
        }else{//不为文件，则为文件夹
            String[] childFilePath = file.list();//获取文件夹下所有文件相对路径
            if(childFilePath!=null){
                for (String path:childFilePath){
                    File childFile= new File(file.getAbsoluteFile()+"/"+path);
                    deleteFile(childFile);//递归，对每个都进行判断
                }
                file.delete();
            }
        }
    }

    /**
     * 创建文件
     * @param fileDir 文件目录
     * @throws Exception
     */
    public  static File  createFile(String fileDir)throws Exception{
        File file=new File(fileDir);
        if(!file.getParentFile().exists() && !file.exists()){
            file.getParentFile().mkdirs();
            file.createNewFile();
        } else if (!file.exists()) {
            file.createNewFile();
        }
        return file;
    }

    /**
     * 创建文件
     * @param filePath 根路径
     * @param fileName 文件名
     * @return
     */
    public static File createFile(String filePath,String fileName)throws Exception{
        String fileDir=filePath+File.separator+fileName;
        return createFile(fileDir);
    }

    /**
     * 从输入流中获取字节数组
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
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
            byte[] data = readInputStream(inStream);
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
