//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.hadesstartool;

import java.io.File;
import java.io.Serializable;

//游戏路径
public class toPathData implements Serializable {
    File file;
    String fileName;

    public toPathData(File var1, String var2) {
        this.file = var1;
        this.fileName = var2;
    }

    public File getFile() {
        return this.file;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFile(File var1) {
        this.file = var1;
    }

    public void setFileName(String var1) {
        this.fileName = var1;
    }

    public String toString() {
        return this.fileName;
    }
}
