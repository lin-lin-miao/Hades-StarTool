//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.hadesstartool;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Looper;
import android.os.Build.VERSION;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;

public class TestAccount extends Thread {
    private File adp = null;
//    Copy co;
    private Context context = null;
//    dataTools dataTools1;
    boolean have_new;
    String nowName;
    String oldName;
    private File res_Account = null;
    private File[] res_Account_f = null;
    private File res_hades = null;
    private File res_rubbish = null;
    int sdkInt;
    private TextView textView = null;
    private File to_Account = null;

    public TestAccount(File var1, File var2, File[] var3, File var4, File var5, File var6, Context var7, TextView var8) {
        this.sdkInt = VERSION.SDK_INT;
        this.oldName = "info";
        this.nowName = "ac";
        this.have_new = true;
        this.res_hades = var1;
        this.res_Account = var2;
        this.res_Account_f = var3;
        this.to_Account = var4;
        this.context = var7;
        this.res_rubbish = var5;
        this.textView = var8;
        this.adp = var6;
//        this.co = new Copy(var7);     ///<<<<
//        this.dataTools1 = new dataTools((Activity)var7, 1);   ///<<<<
    }

    private void copy(int var1) {
        Log.e("库", "onItemClick: 读取:" + this.res_Account_f[var1].getName());
        String var2 = this.res_Account_f[var1].getName();

        label18: {
            String var3;
            try {
                var3 = this.res_Account_f[var1].getName().substring(0, this.res_Account_f[var1].getName().lastIndexOf("."));
            } catch (Exception var4) {
                var4.printStackTrace();
                break label18;
            }

            var2 = var3;
        }


//<<<<<<<<<<<
//        if (this.co.copy(this.res_Account, this.res_rubbish, this.res_Account_f[var1].getName(), this.res_Account_f[var1].getName())) {
//            Toast.makeText(this.context, var2 + "已清除", 1).show();
//        } else {
//            Toast.makeText(this.context, var2 + "清除失败", 1).show();
//        }

    }

    private void copy1(int var1, String var2) {
        Log.e("库", "onItemClick: 读取:" + this.res_Account_f[var1].getName());
        String var3 = this.res_Account_f[var1].getName().substring(0, this.res_Account_f[var1].getName().lastIndexOf("."));
//        ///<<<<<<<<<<<<<<<
//        Copy var4 = this.co;
//        File var5 = this.res_Account;
//        if (var4.copy(var5, var5, this.res_Account_f[var1].getName(), var2 + "." + this.nowName)) {
//            Toast.makeText(this.context, var3 + "已导入", 0).show();
//        } else {
//            Toast.makeText(this.context, var3 + "导入失败", 0).show();
//        }

    }

    public boolean checkFile(File param1, File param2) {
        // $FF: Couldn't be decompiled      ///<<<<
        return false;
    }

    public void checkMistake() {
        boolean var1 = false;
        int var2 = 0;

        while(true) {
            File[] var3 = this.res_Account_f;
            if (var2 >= var3.length) {
                if (var1) {
                    Toast.makeText(this.context, "文件检查器:发现异常，建议重启", Toast.LENGTH_LONG).show();
                }

                Log.e("te", "checkMistake: 库异常文件检查完成");
                return;
            }

            boolean var6;
            if (var3[var2].isFile()) {
                String var4 = this.res_Account_f[var2].getName();
                String var8 = this.res_Account_f[var2].getName();

                String var5;
                label30: {
                    try {
                        var5 = this.res_Account_f[var2].getName().substring(0, this.res_Account_f[var2].getName().lastIndexOf("."));
                    } catch (Exception var7) {
                        var7.printStackTrace();
                        break label30;
                    }

                    var8 = var5;
                }

                var5 = var4.substring(var4.lastIndexOf(".") + 1, var4.length());
                var6 = var1;
                if (!var5.equals(this.nowName)) {
                    if (var5.equals(this.oldName)) {
                        this.copy1(var2, var8);
                        this.res_Account_f[var2].delete();
                        var6 = var1;
                    } else {
                        this.copy(var2);
                        var6 = true;
                        this.res_Account_f[var2].delete();
                    }
                }
            } else {
                this.res_Account_f[var2].delete();
                var6 = true;
            }

            ++var2;
            var1 = var6;
        }
    }

    public void checkNew() {
        File var1 = new File(this.to_Account, "login.info");
        if (this.sdkInt >= 30) {
            //<<<<<<<<dataTools
//            if (this.dataTools1.checkData(this.adp.toString(), var1.toString())) {
//                dataTools var2 = this.dataTools1;
//                if (var2.read(var2.interceptData(this.adp.toString(), var1.getParent()), var1.getName()) == null) {
//                    return;
//                }
//            } else if (!var1.exists()) {
//                return;
//            }
        } else if (!var1.exists()) {
            return;
        }

        if (this.res_Account_f.length == 0 && this.to_Account.exists()) {
            this.haveNewAccount();
        } else {
            File[] var5 = this.res_Account_f;
            int var3 = var5.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                var1 = var5[var4];
                this.checkFile(this.to_Account, var1);
                if (!this.have_new) {
                    return;
                }
            }

            this.haveNewAccount();
        }
    }

    public void haveNewAccount() {
        Log.e("warn", "haveNewAccount:发现新账号警告");
        //<<<<<<提示弹窗无图片
//        new Builder(this.context).setIcon(2131231012).setTitle("Tips").setMessage("检测出未保存的账号\n请导入新账号").setPositiveButton("导入新账号", new OnClickListener() {
//            public void onClick(DialogInterface var1, int var2) {
//                Log.e("warn", "onClick: 导入新账号");
//                TestAccount.this.newAccount();
//            }
//        }).setNegativeButton("取消", new OnClickListener() {
//            public void onClick(DialogInterface var1, int var2) {
//                Log.e("warn", "onClick: 取消");
//            }
//        }).create().show();
    }

    public void newAccount() {
        Log.e("te", "newAccount:新建账号");
        //<<<<<<新建账号页面
//        Intent var1 = new Intent(this.context, newAccount.class);
//        var1.putExtra("to_Account_Extra", this.to_Account.toString());
//        var1.putExtra("res_Account_Extra", this.res_Account.toString());
//        this.context.startActivity(var1);
    }

    public void run() {
        Log.e("te", "run: 开始检查");
        Looper.prepare();
        this.checkNew();
        this.checkMistake();
        Looper.loop();
        Log.e("te", "run: 检查完成");
    }

    public void up_day(File var1, File var2, File[] var3, File var4, File var5, Context var6) {
        this.res_hades = var1;
        this.res_Account = var2;
        this.res_Account_f = var3;
        this.to_Account = var4;
        this.context = var6;
        this.res_rubbish = var5;
    }
}
