//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.hadesstartool;

import android.os.Environment;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Res_Setting implements Serializable {
    private static final long serialVersionUID = 100204L;
    public String error = null;
    public String first_start = "0";
    public int nextDay = 0;
    public int nextHour = 0;
    private File res_Account = null;
    private File to_Account = new File(Environment.getExternalStorageDirectory(), "Android//data//com.ParallelSpace.Cerberus//files");
    public List<toPathData> to_path = new ArrayList();

    public Res_Setting() {
    }

    public File getRes_Account() {
        return this.res_Account;
    }

    public File getTo_Account() {
        return this.to_Account;
    }

    public List<toPathData> get_Lto_path() {
        return this.to_path;
    }

    public void setRes_Account(File var1) {
        this.res_Account = new File(String.valueOf(var1));
    }

    public void setTo_Account(File var1) {
        this.to_Account = var1;
    }

    public void set_to_path(List<toPathData> var1) {
        this.to_path = var1;
    }

    public void setting_w(File var1, Res_Setting var2) {///<<<<<设置类写
        IOException var10000;
        label125: {
            ObjectOutputStream var3;
            boolean var10001;
            try {
                FileOutputStream var4 = new FileOutputStream(var1);
                var3 = new ObjectOutputStream(var4);
            } catch (IOException var22) {
                var10000 = var22;
                var10001 = false;
                break label125;
            }

            try {
                var3.writeObject(var2);
            } catch (Throwable var21) {
                Throwable var23 = var21;

                try {
                    var3.close();
                } catch (Throwable var19) {
                    Throwable var25 = var19;

//                    label109:
//                    {
//                        var23.addSuppressed(var25);
//                        break label109;
//                    }
                }

                try {
                    throw var23;
                } catch (IOException var17) {
                    var10000 = var17;
                    var10001 = false;
                    break label125;
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }

            try {
                var3.close();
                return;
            } catch (IOException var20) {
                var10000 = var20;
                var10001 = false;
            }
        }

        IOException var24 = var10000;
        var24.printStackTrace();
    }

    public void to_path_add(File var1) {
        toPathData var2 = new toPathData(var1, String.valueOf(this.to_path.size() + 1));
        this.to_path.add(var2);
    }

    public void to_path_add(File var1, String var2) {
        toPathData var3 = new toPathData(var1, var2);
        this.to_path.add(var3);
    }

    public toPathData to_path_get(int var1) {
        return (toPathData)this.to_path.get(var1);
    }

    public void to_path_remove(int var1) {
        this.to_path.remove(var1);
    }
}
