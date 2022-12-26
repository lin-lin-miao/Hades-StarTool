package com.example.hadesstartool;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Utils.FileUtils;

/** 加密账号 **/
public class AccountPack implements Serializable {
    private static final long serialVersionUID = 43397387435L;
    String name;
    byte[] password;
    File file;
    int ranking;


    private List<byte[]> contents;//文件内容
    private List<Integer> contentsLen;//输出索引

    public AccountPack(String name,File input,String password){
        this.password = password.getBytes(StandardCharsets.UTF_8);

        this.name = name;
        file = new File(GP.resAccount,name);

        upload(input);
    }
    //检测是否相同
    public boolean check(AccountPack ap){
        if(contents.size()!=ap.contents.size()||contentsLen.size()!=ap.contentsLen.size()){
            return false;
        }

        for (int i = 0;i<contents.size();i++){
            if(!Arrays.equals(contents.get(i), ap.contents.get(i))) {
                return false;
            }
        }

        return true;
    }

    /**
     * 将文件内容上传到该对象
     * @param input 文件地址(包括文件名)
     * @return true/成功    false/失败
     */
    public boolean upload(File input){

            try(BufferedInputStream bis = new BufferedInputStream(new FileInputStream(input),1024)) {
            contents = new ArrayList<>();
            contentsLen = new ArrayList<>();
            byte[] bys = new byte[1024];
            int len;
            while ((len=bis.read(bys)) != -1) {

                contents.add(bys.clone());
                contentsLen.add(len);
                // System.out.println(len);
            }
            bis.close();
            GP.BR.add("账号"+name+"录入成功");
            return true;
        } catch (Exception e) {
            //TODO: handle exception
            e.printStackTrace();
            GP.BR.add("账号"+name+"录入失败");
            return false;
        }
    }

    /**
     * 将该对象保存的文件内容下载
     * @param output 保存地址(不含文件名)
     * @return true/成功    false/失败
     */
    public boolean download(File output){
        try(BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(output),1024);) {
            for (int i = 0; i < contents.size(); i++) {
                //byte[] bys = new byte[1];
                bos.write(contents.get(i), 0,contentsLen.get(i).intValue());
                // System.out.println(contentsLen.get(i).intValue());
            }
            bos.close();
            contents.clear();
            GP.BR.add("载入成功");
            return true;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            GP.BR.add("载入失败"+e.getMessage());
            return false;
        }
    }

    /**
     * 将该对象序列化保存到本地
     * @param writePath 保存地址
     * @return true/成功    false/失败
     */
    public boolean writeLocal(File writePath){
        File write = new File(writePath,name+".AP");
        try (ObjectOutputStream oops = new ObjectOutputStream(new FileOutputStream(write)))
        {
            oops.writeObject(this);
            oops.close();
            GP.BR.add("保存成功");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            GP.BR.add("保存失败"+e.getMessage());
            return false;
        }
    }

    public AccountPack readLocal(File readPath){
        File read = new File(readPath,name);
        try {
            ObjectInputStream oips = new ObjectInputStream(new FileInputStream(read));
            AccountPack r = (AccountPack) oips.readObject();
            oips.close();
            return r;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            GP.BR.add("读取失败"+e.getMessage());
            return null;
        }
    }

    public boolean reName(String newName){
        name = newName;
        FileUtils.delete(file);
        file = new File(GP.resAccount,name);
        return writeLocal(GP.resAccount);
    }
}
