package Utils;

import android.os.Build;

import androidx.annotation.NonNull;

import com.example.hadesstartool.GP;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 日志记录
 **/
public class BugRecorder {
    public boolean have_BigBug = false;
    public List<BugData> bugDataList = new ArrayList<>();

    public BugRecorder() {
        add("onStartApp");
        add("品牌:" + Build.BRAND);
        add("release版本:" + Build.VERSION.RELEASE);
        add("API版本:" + Build.VERSION.SDK_INT);

    }

    public void add(String bug) {
        bugDataList.add(new BugData(bug));
    }

    public List<BugData> getData() {
        return bugDataList;
    }

    public boolean endSave(boolean append){
        File CacheDir = GP.mainActivity.getExternalCacheDir();
        add("end");
        saveTxt(append);
        return FileUtils.Copy.fliePortName(CacheDir,CacheDir,"log.txt","nextLog.txt",true);
    }

    public boolean saveTxt(boolean append) {
        return saveTxt(GP.mainActivity.getExternalCacheDir().toString().toString() + "/log.txt", append);
    }

    public boolean saveTxt(String path, boolean append) {
        if (!FileUtils.writeTxt(path, new BugData("<保存").toString() + '\n', append)) {
            return false;
        }
        List<BugData> bugDataListC = new ArrayList<>(bugDataList);
        bugDataList.clear();
        for (BugData bugData : bugDataListC) {
            if (!FileUtils.writeTxt(path, '\t' + bugData.toString() + '\n', true)) {
                return false;
            }
        }
        bugDataListC.clear();
        return true;
    }

    public static class BugData {
        String data;

        public BugData(String text) {
            DateUtils date = new DateUtils();
            data = date + ":" + text;
        }

        @NonNull
        @Override
        public String toString() {
            return data;
        }
    }
}


