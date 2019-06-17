package com.yxjh.api.file;

import java.io.File;

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

}
