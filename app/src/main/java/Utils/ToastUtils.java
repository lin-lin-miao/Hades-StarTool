package Utils;

import android.content.Context;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.example.hadesstartool.GP;

public class ToastUtils {
    private static final String TAG = "ToastUtils";
    private Context context;

    public ToastUtils(Context context){
        this.context = context;
    }

    public void debug(String text){
        toast_debug(context,text);
    }

    public static void debug(Context context,String text){
        debug(context,text,TAG,"toast_debug");
    }

    public static void debug(Context context,String toastText,String TAG,String msg){
        Log.e(TAG, msg+":"+toastText);
        toast_debug(context,toastText);
    }

    public static void toast_debug(Context context,String text){
        if(GP.onDebug){
            toast(context,text);
        }
    }

    public static void toast(Context context, String text){
        toast(context,text, Toast.LENGTH_SHORT);
    }

    public static void toast(Context context,String text,int time){
        Thread toastThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(context, text, time).show();
                Looper.loop();
            }
        });
        toastThread.setName("toastThread");
        toastThread.start();
    }
}
