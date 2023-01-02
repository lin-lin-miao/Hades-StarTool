package com.example.hadesstartool;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AboutInfo extends AppCompatActivity {

    private static final String infoString =
            "\n更新日志:1.3.1\n\t1.解决安卓11以上无法访问目录问题\n" +
                    "\t2.优化授权方案\n" +
                    "\t3.解决安卓11无法自动跳转游戏\n" +
                    "\t4.修复已知bug\n" +
            "\n更新日志:1.3\n\t1.由于上版本源码遗失,该版本为重制\n" +
            "\t2.优化授权方案\n" +
            "\t3.优化代码逻辑\n" +
            "\t4.重制界面\n" +
            "\t5.账号可根据首字0-9-AZ排序\n" +
            "\t6.一键换号(未能关闭在前台运行的游戏,需将游戏转为后台)\n" +
            "\t7.设定功能暂时移除后续版本可能重制\n" +
            "\n\n\n仅在深空补给港频道或qq群发布\n" +
            "若发现bug请到频道或qq群反馈意见\n" +
            "QQ:879206242(深空补给港)\n" +
            "-----by:靈凛\n" +
            "(存储权限仅用于访问账号与目录库,授权即为同意访问)\n" +
            "\n\n\n\n\n\n\n\n\n喵~\n";

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_info);
        EditText info = findViewById(R.id.ET_about_info);
        info.setKeyListener(null);

        info.setText(infoString);
        Button btn_joinQQ = findViewById(R.id.btn_joinQQ);
        btn_joinQQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!joinQQGroup(AboutInfo.this.getString(R.string.joinQQGroup_key))) {
                    Toast.makeText(AboutInfo.this, "呼起QQ失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Button btn_afd = findViewById(R.id.btn_afd);
        btn_afd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_afd.setText("投喂靈凛");
                Intent intent = new Intent();
//Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse(AboutInfo.this.getString(R.string.afd_http));
                intent.setData(content_url);
                startActivity(intent);
            }
        });
        Button btn_break = findViewById(R.id.btn_break);
        btn_break.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    /****************
     *
     * 发起添加群流程。群号：Hades`StarTool(深空补给港(879206242) 的 key 为： VcsZvPOVVvMmbKHwrjbHMI-dbwEHkw26
     * 调用 joinQQGroup(VcsZvPOVVvMmbKHwrjbHMI-dbwEHkw26) 即可发起手Q客户端申请加群 Hades`StarTool(深空补给港(879206242)
     *
     * @param key 由官网生成的key
     * @return 返回true表示呼起手Q成功，返回false表示呼起失败
     ******************/
    public boolean joinQQGroup(String key) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26jump_from%3Dwebapi%26k%3D" + key));
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            startActivity(intent);
            return true;
        } catch (Exception e) {
            // 未安装手Q或安装的版本不支持
            return false;
        }
    }

}