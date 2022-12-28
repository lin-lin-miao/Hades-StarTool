package com.example.hadesstartool;

import android.annotation.SuppressLint;
import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import Utils.BugRecorder;
import Utils.dataTools;

/**
 * 全局参数
 **/
public class GP {
    static public boolean firstStart = true;
    static public boolean onDebug = false;

    static public String version = "1.3.0";

    static final public String TAG_Storage = "Storage";

    static public BugRecorder BR = new BugRecorder();
    @SuppressLint("StaticFieldLeak")
    static public MainActivity mainActivity;
    @SuppressLint("StaticFieldLeak")
    static public Utils.dataTools dataTools;

    static public final String Android_data = "Android/data/";
    static public File mainFile = new File(Environment.getExternalStorageDirectory(), "HadesTool");
    static public File settingFile;
    static public File resAccount = new File(mainFile, "resAccount");
    static public File rubbish = new File(mainFile, "rubbish");
    static public File to_Account = new File(Environment.getExternalStorageDirectory(), "Android//data//com.ParallelSpace.Cerberus//files//login.info");

    static public toPathData to_path = new toPathData(to_Account, "Cerberus");
    static public List<toPathData> to_pathList = new ArrayList();


//    static public File[] accountList;
    static public List<File> accountList = new ArrayList<>();
    static public List<Integer> errorIntList = new ArrayList<>();


    static public final int Insufficient_Permissions = 100;
    static public final int Game_Directory_Not_Exist = 101;
    static public final int No_Record = 102;

    //requestCode
    static public final int PERMISSION_REQUEST = 1000;
    static public final int Manage_All_File = 1001;
    static public final int requestDataToolCode = 1002;
    static public final int Directory_Selection = 1003;




}
