package com.example.hadesstartool;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Looper;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Arrays;

import Utils.FileUtils;
import Utils.StringUtils;
import Utils.ToastUtils;

public class CheckAccount implements Runnable{

    @Override
    public void run() {
        GP.BR.add("检查目录");
        checkResError();

        checkAccount();
    }

    public static void checkAccount(){
        File[] accountList = GP.resAccount.listFiles();
        File onA = GP.to_path.file;
        if(!onA.exists()){
            GP.BR.add("无载入账号");
            GP.mainActivity.setBtn_onLoad("无载入账号");
            return;
        }
        if(accountList==null || accountList.length==0){
            GP.BR.add("目录无文件");
            return;
        }
        for (File file:accountList){
            if(FileUtils.checkContent(file,onA)){
                //<<<检测到相同
                GP.mainActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        GP.mainActivity.setBtn_onLoad(StringUtils.getFileName(file));
                    }
                });
                return;
            }
        }
        //<<<未录入的
        ToastUtils.toast(GP.mainActivity,"检测到未录入账号");

        GP.mainActivity.runOnUiThread(new Runnable() {
            public void run() {
                inputName(accountList,onA);
            }
        });


    }

    public static void inputName(File[] accountList,File onA){
        View view = GP.mainActivity.getLayoutInflater().inflate(R.layout.input_dialog_view, null);
        EditText editText = (EditText) view.findViewById(R.id.ET_input);
        AlertDialog dialog = new AlertDialog.Builder(GP.mainActivity)
//                            .setIcon(R.mipmap.icon)//设置标题的图片
                .setTitle("检测到未录入账号")//设置对话框的标题
                .setMessage("请输入账号名称")
                .setView(view)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = editText.getText().toString();
                        if(name.equals("")){
                            ToastUtils.toast(GP.mainActivity, "无效名称", Toast.LENGTH_SHORT);
                            dialog.dismiss();
                            checkAccount();
                            return;
                        }
                        for(File file:accountList){
                            String acName = StringUtils.getFileName(file);
                            if(acName.equals(name)){
                                ToastUtils.toast(GP.mainActivity,"该名称已存在");
                                dialog.dismiss();
                                checkAccount();
                                return;
                            }
                        }
                        FileUtils.Copy.fileToPortRename(onA,GP.resAccount,name+".ac");
                        GP.accountList.clear();
                        GP.accountList.addAll(Arrays.asList(FileUtils.Sort.sortFilesA_Z(GP.resAccount.listFiles())));
                        GP.mainActivity.accountRecyclerViewAdapter.notifyDataSetChanged();
                        ToastUtils.toast(GP.mainActivity, "已添加"+name, Toast.LENGTH_SHORT);
                        checkAccount();
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();
    }

    @SuppressLint("NotifyDataSetChanged")
    public static void checkResError(){
        File[] accountList = GP.resAccount.listFiles();
        if(accountList==null || accountList.length==0){
            GP.BR.add("目录无文件");
            return;
        }
        for (File file:accountList){
            if(!file.exists())continue;
            String name = StringUtils.getFileName(file);
            String extension = StringUtils.getFileExtension(file);
            switch (extension){
                case "info":
                    if(name.equals("login")){
                        //<<<弹窗命名
                        GP.mainActivity.runOnUiThread(new Runnable() {
                            public void run() {
                                View view = GP.mainActivity.getLayoutInflater().inflate(R.layout.input_dialog_view, null);
                                EditText editText = (EditText) view.findViewById(R.id.ET_input);
                                AlertDialog dialog = new AlertDialog.Builder(GP.mainActivity)
//                            .setIcon(R.mipmap.icon)//设置标题的图片
                                        .setTitle("检测到login.info账号")//设置对话框的标题
                                        .setMessage("请输入账号名称")
                                        .setView(view)
                                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                            @SuppressLint("NotifyDataSetChanged")
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                FileUtils.Copy.fileToPortRename(file,GP.resAccount,name+".ac");
                                                ToastUtils.toast(GP.mainActivity,name+"录入成功");
                                                FileUtils.delete(file);
                                                GP.accountList.clear();
                                                GP.accountList.addAll(Arrays.asList(FileUtils.Sort.sortFilesA_Z(GP.resAccount.listFiles())));
                                                GP.mainActivity.accountRecyclerViewAdapter.notifyDataSetChanged();

                                                dialog.dismiss();
                                            }
                                        })
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @SuppressLint("NotifyDataSetChanged")
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                String name = editText.getText().toString();
                                                if(name.equals("")){
                                                    ToastUtils.toast(GP.mainActivity, "无效名称", Toast.LENGTH_SHORT);
                                                    dialog.dismiss();
                                                    checkResError();
                                                    return;
                                                }
                                                for(File file:accountList){
                                                    String acName = StringUtils.getFileName(file);
                                                    if(acName.equals(name)){
                                                        ToastUtils.toast(GP.mainActivity,"该名称已存在");
                                                        dialog.dismiss();
                                                        checkResError();
                                                        return;
                                                    }
                                                }
                                                FileUtils.Copy.fileToPortRename(file,GP.resAccount,name+".ac");
                                                ToastUtils.toast(GP.mainActivity, "已添加"+name, Toast.LENGTH_SHORT);
                                                FileUtils.delete(file);
                                                GP.accountList.clear();
                                                GP.accountList.addAll(Arrays.asList(FileUtils.Sort.sortFilesA_Z(GP.resAccount.listFiles())));
                                                GP.mainActivity.accountRecyclerViewAdapter.notifyDataSetChanged();

                                                checkResError();
                                                dialog.dismiss();
                                            }
                                        }).create();
                                dialog.show();
                            }
                        });
                    }else {
                        FileUtils.Copy.fileToPortRename(file,GP.resAccount,name+".ac");
                        ToastUtils.toast(GP.mainActivity,name+"录入成功");
                        FileUtils.delete(file);
                    }
                    break;
                case "ac":
                    break;
                case "AP":
                    break;
                default:
                    if(FileUtils.Copy.fileToPortRename(file, GP.rubbish,file.getName())){
                        ToastUtils.toast(GP.mainActivity,"异常文件"+file.getName()+"已移动至"+GP.rubbish);
                        GP.BR.add("异常文件"+file.getName()+"已移动至"+GP.rubbish);
                        FileUtils.delete(file);
                    }
                    break;
            }
        }
        GP.accountList.clear();
        GP.accountList.addAll(Arrays.asList(FileUtils.Sort.sortFilesA_Z(GP.resAccount.listFiles())));
        GP.mainActivity.accountRecyclerViewAdapter.notifyDataSetChanged();
    }

}
