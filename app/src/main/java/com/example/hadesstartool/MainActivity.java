package com.example.hadesstartool;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.documentfile.provider.DocumentFile;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import Utils.BugRecorder;
import Utils.FileUtils;
import Utils.SpacesItemDecoration;
import Utils.StringUtils;
import Utils.ToastUtils;
import Utils.dataTools;

public class MainActivity extends AppCompatActivity {




    Button btn_leftMore;
    Button btn_onLoad;
    Button btn_toPhat;

    Button btn_newAccount;
    Button btn_Calculator;
    Button btn_recorder;
    Button btn_about;

    Button btn_author;

    RecyclerView accountRecycler;
    AccountRecyclerViewAdapter accountRecyclerViewAdapter = new AccountRecyclerViewAdapter();

    ErrorRecyclerViewAdapter errorRecyclerViewAdapter;
    DrawerLayout drawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GP.BR.add("onCreate");
        //<<<绑定界面
        GP.mainActivity = this;
        GP.dataTools = new dataTools(this, GP.requestDataToolCode);
        GP.settingFile = new File(getExternalFilesDir(null), "setting");
        drawerLayout = findViewById(R.id.mainLayout);
        accountRecycler = findViewById(R.id.accountRecycler);

        btn_leftMore = findViewById(R.id.btn_leftMore);
        btn_onLoad = findViewById(R.id.btn_onLoad);
        btn_toPhat = findViewById(R.id.btn_toPhat);

        btn_newAccount = findViewById(R.id.btn_newAccount);
        btn_Calculator = findViewById(R.id.btn_Calculator);
        btn_recorder = findViewById(R.id.btn_recorder);
        btn_about = findViewById(R.id.btn_about);

        btn_author = findViewById(R.id.btn_author);

        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        accountRecycler.setLayoutManager(layoutManager);
        accountRecycler.addItemDecoration(new SpacesItemDecoration(25));


        //<<<button动作绑定
        setButton();

        //<<<读取设置数据
        if (GP.firstStart) {
            readSet();
        }
        GP.BR.saveTxt(!GP.firstStart);
    }


    private void setButton() {

        btn_leftMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        btn_onLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String p = StringUtils.getPackage(GP.to_path.file.toString());
                stopGame(p);
                startGame(p);
            }
        });
        btn_toPhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //新le
                choicePath();
            }
        });
        btn_newAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkP()) {
                    if (!GP.to_path.file.exists()) {
                        ToastUtils.toast(MainActivity.this, "账号已卸载");
                        return;
                    }
                    AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
//                    .setIcon(R.mipmap.icon)//设置标题的图片
                            .setTitle("警告")//设置对话框的标题
                            .setMessage("将卸载当前载入账号请确认已录入")//设置对话框的内容
                            //设置对话框的按钮
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    GP.BR.add("取消");
                                }
                            })
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String p = StringUtils.getPackage(GP.to_path.file.toString());
                                    GP.BR.add("确定卸载");
                                    stopGame(p);
                                    FileUtils.delete(GP.to_path.file);
                                    startGame(p);
                                    dialog.dismiss();
                                }
                            }).create();
                    dialog.show();
                } else {
                    Toast.makeText(MainActivity.this, "无操作权限", Toast.LENGTH_SHORT).show();
                }

            }
        });
        btn_Calculator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //<<
                ToastUtils.toast(MainActivity.this, "正在开发");
            }
        });
        btn_recorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //<<
                ToastUtils.toast(MainActivity.this, "正在开发");
//                reStart();
            }
        });
        btn_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBtn_about();
            }
        });

        btn_author.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_author.setText(MainActivity.this.getString(R.string.by_author));
                ToastUtils.toast(MainActivity.this, hide());
            }
        });

    }

    private final String[] qwq = {"阿巴阿巴", "qwq", "喵~", "喵~", "喵~", "喵~", "喵~",
            "敲敲敲", "哼", "咬", "rua!", "笨蛋","坏蛋", "hentai", "バカバカ"};

    public String hide() {
        GP.BR.add("hide()");
        Random random = new Random();
        int ran = random.nextInt(7);
        switch (ran) {
            case 0:
                return new Sentence(random.nextInt(24)).run();
            case 1:
                return "喵~";
            case 2:
                if (random.nextInt(2) == 0) {
                    if (GP.onDebug) {
                        ToastUtils.debug(MainActivity.this, "Debug模式已关闭");
                        GP.onDebug = false;
                    } else {
                        GP.onDebug = true;
                        ToastUtils.debug(MainActivity.this, "Debug模式已开启");
                    }
                }
                return "敲敲敲";
            case 3:
                return qwq[random.nextInt(qwq.length)];
            case 4:
                setBtn_about();
                return "多看看";
            case 5:
                if (GP.accountList == null || GP.accountList.size() < 1) {
                    return "饿饿";
                } else {
                    return StringUtils.getFileName(GP.accountList.get(random.nextInt(GP.accountList.size()))) + " 被吃掉惹";
                }
            default:
                reStart();
                return "坏掉了";
        }
    }

    public void setBtn_about() {
        GP.BR.add("关于");
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, AboutInfo.class);
        startActivity(intent);
    }

    /**
     * 跳转到路径选择
     **/
    public void choicePath() {
        GP.BR.add("选择路径");
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, ChoicePath.class);
        startActivity(intent);
    }

    private void readSet() {
        if (!GP.settingFile.exists()) {
            //初始化设置
            setBtn_about();
            new SettingInfo().initialize();
            return;
        }
        try {
            ObjectInputStream obis = new ObjectInputStream(new FileInputStream(GP.settingFile));
            SettingInfo o = (SettingInfo) obis.readObject();
            o.read();
            if (!o.version.equals(GP.version)) {
                //显示信息
                setBtn_about();
                o.write();
            }
            obis.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onStart() {
        super.onStart();
        GP.accountList.clear();
        GP.BR.add("onStart");
        GP.errorIntList.clear();
        if (GP.firstStart) {
            //<<<检测权限>>授权弹窗
            checkPermission();
        }
        btn_toPhat.setText(GP.to_path.fileName);
        if (checkP()) {
            File parentFile = GP.to_path.file.getParentFile();
            if (parentFile == null || !GP.dataTools.dirIsExist("/"+StringUtils.cutEnd(GP.Android_data,parentFile.toString()))) {
//            if (!GP.dataTools.dirIsExist("/"+StringUtils.cutEnd(GP.Android_data,GP.to_path.file.toString()))) {
                //提示游戏目录不存在
                ToastUtils.toast(this, "游戏目录不存在");
                GP.BR.add("游戏目录不存在");
                GP.errorIntList.add(GP.Game_Directory_Not_Exist);
                errorRecyclerViewAdapter = new ErrorRecyclerViewAdapter();
                accountRecycler.setAdapter(errorRecyclerViewAdapter);
            } else {
                GP.mainFile.mkdirs();
                GP.resAccount.mkdirs();
                GP.rubbish.mkdirs();

                //检测账号
                CheckAccount.checkResError();
                CheckAccount.checkAccount();

                GP.accountList.clear();
                GP.accountList.addAll(Arrays.asList(FileUtils.Sort.sortFilesA_Z(GP.resAccount.listFiles())));
                if (GP.accountList.size() < 1) {
                    GP.errorIntList.add(GP.No_Record);
                    errorRecyclerViewAdapter = new ErrorRecyclerViewAdapter();
                    accountRecycler.setAdapter(errorRecyclerViewAdapter);
                } else {

                    //打印账号
                    accountRecyclerViewAdapter.notifyDataSetChanged();
                    accountRecycler.setAdapter(accountRecyclerViewAdapter);

                }

            }
        } else {
            //权限不足
            GP.errorIntList.add(GP.Insufficient_Permissions);
            errorRecyclerViewAdapter = new ErrorRecyclerViewAdapter();
            accountRecycler.setAdapter(errorRecyclerViewAdapter);

        }


        GP.BR.saveTxt(true);
        GP.firstStart = false;
    }

    public void setBtn_onLoad(String name) {

        btn_onLoad.setText(name);
    }

    public void stopGame(String packageName) {
        ActivityManager mActivityManager = (ActivityManager)
                GP.mainActivity.getSystemService(Context.ACTIVITY_SERVICE);
        GP.BR.add("关闭:" + packageName);
        mActivityManager.killBackgroundProcesses(packageName);
//        mActivityManager.forceStopPackage
        Method method = null;
        try {
            method = Class.forName("android.app.ActivityManager").getMethod("forceStopPackage", String.class);
            method.invoke(mActivityManager, packageName);
        } catch (NoSuchMethodException | ClassNotFoundException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void startGame(String packageName) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    Intent LaunchIntent = GP.mainActivity.getPackageManager().getLaunchIntentForPackage(packageName);
//                            LaunchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    GP.BR.add("启动:" + packageName);
                    GP.mainActivity.startActivity(LaunchIntent);
                } catch (Exception e) {
                    e.printStackTrace();
                    GP.BR.add(e.getMessage());
                }

            }
        }, 1000);// 1秒钟后重启应用
    }

    @Override
    protected void onPause() {
        super.onPause();
        GP.BR.add("onPause");
        GP.BR.saveTxt(true);
    }

    @Override
    protected void onStop() {
        super.onStop();
        GP.BR.add("onStop");
        new SettingInfo().write();
        GP.BR.saveTxt(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GP.BR.add("onDestroy");
        new SettingInfo().write();
        GP.BR.saveTxt(true);
//        GP.BR.endSave(true);
    }

    String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    List<String> mPermissionList = new ArrayList<>();


    // 检查权限

    /**
     * 完成授权返回ture
     **/
    public boolean checkP() {
        mPermissionList.clear();
        //判断哪些权限未授予
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(permission);
//                GP.BR.add("未授权:" + permission);
            }
        }

        if (!GP.dataTools.isPermissions()) {
            return false;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && !Environment.isExternalStorageManager()) {
            return false;
        } else {
            return true;
        }
        //未授予的权限为空，表示都授予了
        //            GP.BR.add("已有授权");
//        return mPermissionList.isEmpty();
    }

    // 检查权限
    public void checkPermission() {
        mPermissionList.clear();
        //判断哪些权限未授予
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(permission);
                GP.BR.add("未授权:" + permission);
            }
        }

        /**
         * 判断是否为空
         */
        if (mPermissionList.isEmpty()) {//未授予的权限为空，表示都授予了
            GP.BR.add("已有授权");
        } else {//请求权限方法
            String[] permissions = mPermissionList.toArray(new String[mPermissionList.size()]);//将List转为数组
            ActivityCompat.requestPermissions(MainActivity.this, permissions, GP.PERMISSION_REQUEST);
            GP.BR.add("请求权限");
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (!GP.dataTools.isPermissions()) {
                ToastUtils.debug(this, "无data存储权限");
                GP.BR.add("无data存储权限");

                AlertDialog dialog = new AlertDialog.Builder(this)
//                    .setIcon(R.mipmap.icon)//设置标题的图片
                        .setTitle("无data访问权限")//设置对话框的标题
                        .setMessage("请授权data目录\n在弹出的页面选择data目录")//设置对话框的内容
                        //设置对话框的按钮
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                GP.BR.add("取消授权");
                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                GP.BR.add("确定授权");
                                GP.dataTools.requestPermission();
                                dialog.dismiss();
                            }
                        }).create();
                dialog.show();
            } else {
                GP.BR.add("已有data存储权限");
            }

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R || Environment.isExternalStorageManager()) {

//                Toast.makeText(this, "已获得访问所有文件权限", Toast.LENGTH_SHORT).show();
                GP.BR.add("已获得访问所有文件权限");
            } else {
                ToastUtils.debug(this, "无外部目录存储权限");
                GP.BR.add("无外部目录存储权限");
                AlertDialog dialog = new AlertDialog.Builder(this)
//                    .setIcon(R.mipmap.icon)//设置标题的图片
                        .setTitle("无外部目录存储权限")//设置对话框的标题
                        .setMessage("请授权允许管理所有文件\n(外部目录为" + GP.mainFile.toString() + ")\n在弹出的页面选择允许管理所有文件")//设置对话框的内容
                        //设置对话框的按钮
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                GP.BR.add("取消授权");
                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                GP.BR.add("确定授权");

                                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                                intent.setData(Uri.parse("package:" + getPackageName()));
                                startActivityForResult(intent, GP.Manage_All_File);

                                dialog.dismiss();
                            }
                        }).create();
                dialog.show();
            }
        }


    }

    /**
     * 响应授权
     * 这里不管用户是否拒绝，都进入首页，不再重复申请权限
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case GP.PERMISSION_REQUEST:
                GP.BR.add("获得授权");
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        GP.dataTools.savePermissions(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            GP.BR.add(data.toString());
            switch (requestCode) {
                case GP.requestDataToolCode:
                    GP.dataTools.savePermissions(requestCode, resultCode, data);
                    GP.BR.add(data.toString());
                    break;
                case GP.Manage_All_File:
                    GP.BR.add("获得管理所有文件权限");
                    break;

                default:

                    break;
            }
        } else {
            GP.BR.add(resultCode + "回调异常");
        }
    }

    /**
     * 重启当前Activity
     **/
    public void reStart() {
        Intent intent = getIntent();
//        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        GP.BR.add("正在重启");
        ToastUtils.toast(this, "正在重启", Toast.LENGTH_SHORT);
        finish();
        startActivity(intent);
    }

    //定义 请求返回码

    public void openFileManager(int REQUEST_CODE) {
        Uri uri1 = Uri.parse("content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fdata");
        uri1 = DocumentFile.fromTreeUri(this, uri1).getUri();
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");//设置类型，我这里是任意类型，可以过滤文件类型
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, uri1);
        startActivityForResult(intent, REQUEST_CODE);
    }


    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private final String[] exitString = {"拜拜", "再按一次退出程序", "再按一次退出程序", "再按一次退出程序",};

    public void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(getApplicationContext(), exitString[new Random().nextInt(exitString.length)],
                    Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            new SettingInfo().write();
            GP.BR.endSave(true);
            finish();
            System.exit(0);
        }
    }


}