package Utils;

import java.io.File;

/**
 * 字符串操作工具类
 * @author 靈凛
 * @version jdk:18.0.2
 * <p>版本:1.0
 */
public class StringUtils {


    /**
     * 截取Android包名
     * @param filePath Android/data/包名/**
     * @return 包名
     */
    public static String getPackage(String filePath){
        String cut = "Android/data/";
        filePath = cutEnd(cut,filePath);
        try {
            return filePath.substring(0,filePath.indexOf("/"));
        } catch (Exception e) {
            // TODO: handle exception
            return filePath;
        }
    }

    /**
     * 截取后部分字符串
     * @param cut
     * @param s
     * @return
     */
    public static String cutEnd(String cut,String s){
        if(cut.equals("")||cut==null){
            return s;
        }
        try {
            return s.substring(s.indexOf(cut, 0)+cut.length(), s.length());
        } catch (Exception e) {
            // TODO: handle exception
            return s;
        }
    }

    /**
     * 截取前部分字符串
     * @param cut
     * @param s
     * @return
     */
    public static String cutStart(String cut,String s){
        if(cut.equals("")||cut==null){
            return s;
        }
        try {
            return s.substring(0,s.indexOf(cut, 0));
        } catch (Exception e) {
            // TODO: handle exception
            return s;
        }
    }

    /**获取文件名称
     * <p>不能用于获取文件夹名称
     */
    public static String getFileName(File file){
        return getFileName(file.toString());
    }


    /**获取文件名称 */
    public static String getFileName(String filePath){
        String name = filePath;
        try {
            // String fileName = filePath.substring(filePath.lastIndexOf("\\")+1);
            // System.out.println(fileName);
            // name = fileName;
            // name = fileName.substring(0,fileName.lastIndexOf("."));
            try {
                name = filePath.substring(filePath.lastIndexOf("/")+1);
            } catch (Exception e) {
                //TODO: handle exception
            }
            name = name.substring(0,name.lastIndexOf("."));
        } catch (Exception e) {
            //TODO: handle exception
            // System.out.println("StringUtils:"+"getFileName:"+e.getMessage());
            System.out.println("StringUtils:"+"getFileName:"+e.getMessage());
            // e.printStackTrace();
        }
        return name;
    }

    /**获取文件后缀 */
    public static String getFileExtension(File file){
        if(file.isDirectory()){
            return "";
        }
        return getFileExtension(file.toString());
    }

    /**获取文件后缀 */
    public  static String getFileExtension(String filePath){
        String name = filePath;

        try {
            try {
                filePath = filePath.substring(filePath.lastIndexOf("/"));
            } catch (Exception e) {
                //TODO: handle exception
                System.out.println("StringUtils:"+"getFilleExtension:1:"+e.getMessage());
            }

            // String fileName = filePath;
            // System.out.println(filePath.lastIndexOf("."));
            if(filePath.lastIndexOf(".")==-1){
                return "";
            }
            name = filePath.substring(filePath.lastIndexOf(".")+1);

            // System.out.println(filePath);
            if(name.substring(0,1).equals("\\")){
                name = name.substring(1, name.length());
            }

        } catch (Exception e) {
            //TODO: handle exception
            System.out.println("StringUtils:"+"getFilleExtension:2:"+e.getMessage());
            // e.printStackTrace();
        }

        return name;
    }

}
