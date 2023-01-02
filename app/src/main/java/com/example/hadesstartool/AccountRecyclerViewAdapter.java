package com.example.hadesstartool;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Process;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.documentfile.provider.DocumentFile;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.Arrays;

import Utils.FileUtils;
import Utils.StringUtils;
import Utils.ToastUtils;
import Utils.dataTools;

public class AccountRecyclerViewAdapter extends RecyclerView.Adapter<AccountRecyclerViewAdapter.AccountViewHoder> {


    @NonNull
    @Override
    public AccountViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.account_list_item, parent, false);//解决宽度不能铺满

        AccountViewHoder accountViewHoder = new AccountViewHoder(view);
        return accountViewHoder;
    }

    @Override
    public int getItemCount() {
        return GP.accountList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull AccountViewHoder holder, int position) {
        File file = GP.accountList.get(position);
        String name = StringUtils.getFileName(file);
        holder.TV_accountName.setText(name);
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //<<结束游戏
                String packageName =StringUtils.getPackage(GP.to_path.file.toString());
                GP.mainActivity.stopGame(packageName);
//                FileUtils.delete(GP.to_path.file);
                if (FileUtils.Copy.fileToPortRename(file, GP.to_path.file.getParentFile(), "login.info",true)) {
                    GP.BR.add("载入账号");
                    ToastUtils.toast(GP.mainActivity, name + "已载入");
                    GP.mainActivity.startGame(packageName);
                }else {
                    DocumentFile onADF = dataTools.getDocumentFile(GP.mainActivity,GP.to_path.file);
                    DocumentFile res = DocumentFile.fromFile(file);
                    if(onADF == null || !GP.dataTools.writeFileByStream(res,onADF)){
                        GP.BR.add("载入失败");
                        ToastUtils.toast(GP.mainActivity, name + "载入失败");
                    }
                    GP.BR.add("载入账号");
                    ToastUtils.toast(GP.mainActivity, name + "已载入");
                    GP.mainActivity.startGame(packageName);
                }
            }
        });
        holder.rootView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                View view = GP.mainActivity.getLayoutInflater().inflate(R.layout.input_dialog_view, null);
                EditText editText = (EditText) view.findViewById(R.id.ET_input);
                editText.setText(name);
                AlertDialog dialog = new AlertDialog.Builder(GP.mainActivity)
//                            .setIcon(R.mipmap.icon)//设置标题的图片
                        .setTitle("注意!")//设置对话框的标题
                        .setMessage("输入新的名字重命名"+"\n输入DELETE将删除(此操作不可返回)")
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
                                String newName = editText.getText().toString();
                                if(newName.equals("")) return;
                                if(newName.equals(name)) return;
                                if(newName.equals("DELETE")){
                                    FileUtils.delete(file);
                                    ToastUtils.toast(GP.mainActivity,name+"已删除");
                                    GP.accountList.clear();
                                    GP.accountList.addAll(Arrays.asList(FileUtils.Sort.sortFilesA_Z(GP.resAccount.listFiles())));
                                    GP.mainActivity.accountRecyclerViewAdapter.notifyDataSetChanged();
                                }else {
                                    if(FileUtils.Copy.fileToPortRename(file,GP.resAccount,newName+".ac",false)){
                                        FileUtils.delete(file);
                                        ToastUtils.toast(GP.mainActivity,"重命名成功");
                                        GP.accountList.clear();
                                        GP.accountList.addAll(Arrays.asList(FileUtils.Sort.sortFilesA_Z(GP.resAccount.listFiles())));
                                        GP.mainActivity.accountRecyclerViewAdapter.notifyDataSetChanged();
                                    }else {
                                        ToastUtils.toast(GP.mainActivity,"重命名失败(可能包含非法字符)");
                                    }
                                }
                                dialog.dismiss();
                            }
                        }).create();
                dialog.show();
                return true;
            }
        });
    }

    class AccountViewHoder extends RecyclerView.ViewHolder {
        TextView TV_accountName;
        ConstraintLayout rootView;

        public AccountViewHoder(@NonNull View itemView) {
            super(itemView);
            TV_accountName = itemView.findViewById(R.id.TV_accountName);
            rootView = itemView.findViewById(R.id.account_list_item);
        }
    }
}
