package com.yxjh.api.file;

import org.apache.commons.codec.binary.Base64;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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

    /**
     * 单文件压缩
     * @param sourceFile
     * @param zipFile
     * @return
     */
    public static boolean zipUtil(File sourceFile,File zipFile){
        DataInputStream dis = null;
        //输出
        ZipOutputStream zos = null;
        ZipEntry ze = null;
        try {
            if(sourceFile.isFile()){
                //输入-获取数据
                dis=new DataInputStream(new BufferedInputStream(new FileInputStream(sourceFile)));
                //输出-写出数据
                zos=new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipFile),1024));
                ze = new ZipEntry(sourceFile.getName()); //实体ZipEntry保存
                ze.setSize(sourceFile.length());
                ze.setTime(sourceFile.lastModified());
                zos.putNextEntry(ze);
                int len = 0;//临时文件
                byte[] bts = new byte[1024]; //读取缓冲
                while((len=dis.read(bts)) != -1){ //每次读取1024个字节
                    zos.write(bts, 0, len); //每次写len长度数据，最后前一次都是1024，最后一次len长度
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            try {
                if(null!=zos)
                zos.closeEntry();
                if(null!=zos)
                zos.close();
                if(null!=dis)
                dis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    /**
     * 多文件压缩
     * @param fileList
     * @param zipFile
     * @return
     */
    public static boolean zipUtil(List<File>fileList,File zipFile){
        ZipOutputStream zos = null;
        ZipEntry ze=null;
        DataInputStream dis = null;
       try {
           zos=new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipFile)));
           byte[] buf=new byte[1024];
           int readLen=0;
           for(int i = 0; i <fileList.size(); i++) {
               File f=fileList.get(i);
               ze=new ZipEntry(f.getName());
               ze.setSize(f.length());
               ze.setTime(f.lastModified());
               zos.putNextEntry(ze);
               dis=new DataInputStream(new BufferedInputStream(new FileInputStream(f)));
               while ((readLen=dis.read(buf, 0, 1024))!=-1) {
                   zos.write(buf, 0, readLen);
               }
           }
       }catch (Exception e){
        e.printStackTrace();
       }finally {
           try {
               zos.closeEntry();
               zos.close();
               dis.close();
           }catch (Exception e){
               e.printStackTrace();
           }
       }
       return true;
    }

    public static void zipUtil(File sourceFile,File zipFile,boolean keepDirStructure){
        ZipOutputStream zos=null;
        try {
            zos=new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipFile),1024));
            //compress(sourceFile,zos,sourceFile.getName(),keepDirStructure);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public static void main(String[]args)throws Exception{
        File sourceFile1=new File("D:/apache/tomcat/files/szxy/product/my");
        File sourceFile2=new File("D:/apache/tomcat/files/szxy/product/2.jpg");
        List<File>files=new ArrayList<>();
        files.add(sourceFile1);
        files.add(sourceFile2);
        File zipFile=new File("D:/apache/tomcat/files/szxy/product/my.zip");
        //zipUtil(sourceFile1,zipFile,true);
        //zipUtil(files,zipFile);
        //zipUtil(sourceFile1,zipFile,true);
        compressedFile(sourceFile1,zipFile);
       // zipUtil("D:/apache/tomcat/files/asjxs/szxy/product/12.jpg","D:/apache/tomcat/files/asjxs/szxy/product/1.zip");
    }

    public static void compressedFile(File sourceFile,File zipFile) throws Exception{
        FileOutputStream outputStream = new FileOutputStream(zipFile);
        ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(outputStream));
        createCompressedFile(out, sourceFile, "");
        out.close();
    }


    public static void createCompressedFile(ZipOutputStream out,File file,String dir) throws Exception{
        //如果当前的是文件夹，则进行进一步处理
        if(file.isDirectory()){
            //得到文件列表信息
            File[] files = file.listFiles();
            //将文件夹添加到下一级打包目录
            out.putNextEntry(new ZipEntry(dir+"/"));
            dir = dir.length() == 0 ? "" : dir +"/";
            //循环将文件夹中的文件打包
            for(int i = 0 ; i < files.length ; i++){
                createCompressedFile(out, files[i], dir + files[i].getName());
            }
        }else{   //当前的是文件，打包处理
            //文件输入流
            DataInputStream dis=new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
            out.putNextEntry(new ZipEntry(dir));
            //进行写操作
            int j =  0;
            byte[] buffer = new byte[1024];
            while((j = dis.read(buffer)) > 0){
                out.write(buffer,0,j);
            }
            dis.close();
        }
    }
}
