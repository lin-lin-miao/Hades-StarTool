package com.example.hadesstartool;

import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SettingInfo implements Serializable {
    private static final long serialVersionUID = 1564988L;

    public File to_Account = new File(Environment.getExternalStorageDirectory(), "Android//data//com.ParallelSpace.Cerberus//files//login.info");
    public toPathData to_path = new toPathData(to_Account,"Cerberus");
    public boolean onDebug = true;
    public String version = "1.3";

    public List<toPathData> to_pathList = new ArrayList();

    public void initialize(){
        GP.BR.add("初始化设置");
        GP.to_pathList.add(to_path);
        write();
    }

    public void read(){
        GP.BR.add("读取设置");
        GP.to_Account = to_Account;
        GP.onDebug = onDebug;
        GP.to_pathList = to_pathList;
        GP.to_path = to_path;
        if(!version.equals(GP.version)){

        }
    }

    public synchronized void write(){
        version = GP.version;
        to_Account = GP.to_Account;
        onDebug = GP.onDebug;
        to_path = GP.to_path;
        to_pathList = GP.to_pathList;

        try {
            GP.BR.add("写入设置");
            ObjectOutputStream oops = new ObjectOutputStream(new FileOutputStream(GP.settingFile));
            oops.writeObject(this);
            oops.close();
        } catch (IOException e) {
            e.printStackTrace();
            GP.BR.add("写入设置失败"+e.getMessage());
        }
    }

}
