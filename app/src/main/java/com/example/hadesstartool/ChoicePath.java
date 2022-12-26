package com.example.hadesstartool;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.documentfile.provider.DocumentFile;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;

import Utils.FileUtils;
import Utils.SpacesItemDecoration;
import Utils.ToastUtils;

public class ChoicePath extends AppCompatActivity {

    Button btn_toPhat;
    PathRecyclerViewAdapter pathRecyclerViewAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_path);
        RecyclerView recyclerView = findViewById(R.id.pathRecycler);
        recyclerView.addItemDecoration(new SpacesItemDecoration(25));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        pathRecyclerViewAdapter = new PathRecyclerViewAdapter(this);
        recyclerView.setAdapter(pathRecyclerViewAdapter);

        btn_toPhat = findViewById(R.id.btn_toPhat);
        setText(GP.to_path.fileName);
        btn_toPhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });
    }

    public void setText(String name){
        btn_toPhat.setText(name);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            switch (requestCode){
                case GP.Directory_Selection:
                    Uri uri = data.getData();
                    String chooseFilePath = FileUtils.FileChooseUtil.getInstance(this).getChooseFileResultPath(uri);
                    ToastUtils.debug(this, chooseFilePath);
                    File filePath = new File(chooseFilePath);
                    if (!filePath.getName().equals("login.info")) {
                        ToastUtils.toast(this, "请选择游戏目录中的\"login.info\"文件", Toast.LENGTH_LONG);
                        return;
                    }
                    View view = getLayoutInflater().inflate(R.layout.input_dialog_view, null);
                    EditText editText = (EditText) view.findViewById(R.id.ET_input);
                    AlertDialog dialog = new AlertDialog.Builder(this)
//                            .setIcon(R.mipmap.icon)//设置标题的图片
                            .setTitle("请输入路径名称")//设置对话框的标题
                            .setMessage(chooseFilePath)
                            .setView(view)
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String name = editText.getText().toString();
                                    if(name.equals("")){
                                        Toast.makeText(ChoicePath.this, "无效名称", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                        return;
                                    }
                                    GP.to_pathList.add(new toPathData(filePath, name));
                                    pathRecyclerViewAdapter.notifyItemInserted(GP.to_pathList.size()-1);
                                    Toast.makeText(ChoicePath.this, "已添加"+name, Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            }).create();
                    dialog.show();
                    break;
                default:
                    break;
            }
        }
    }

    public void openFileManager(int REQUEST_CODE) {
        Uri uri1 = Uri.parse("content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fdata");
        uri1 = DocumentFile.fromTreeUri(this, uri1).getUri();
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");//设置类型，我这里是任意类型，可以过滤文件类型
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, uri1);
        startActivityForResult(intent, REQUEST_CODE);
    }


}