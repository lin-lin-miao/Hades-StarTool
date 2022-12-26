package com.example.hadesstartool;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import Utils.ToastUtils;

public class PathRecyclerViewAdapter extends RecyclerView.Adapter<PathRecyclerViewAdapter.AccountViewHoder>{

    ChoicePath choicePath;

    public PathRecyclerViewAdapter(ChoicePath choicePath){
        this.choicePath = choicePath;
    }

    @NonNull
    @Override
    public AccountViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.account_list_item, parent, false);//解决宽度不能铺满

        AccountViewHoder accountViewHoder = new AccountViewHoder(view);
        return accountViewHoder;
    }

    @Override
    public int getItemCount() {
        return GP.to_pathList.size()+1;
    }

    @Override
    public void onBindViewHolder(@NonNull AccountViewHoder holder, int position) {
        if(position>=GP.to_pathList.size()){
            holder.TV_accountName.setText("+添加路径+");
            holder.rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //<<选择路径
                    ToastUtils.toast(choicePath, "请选择游戏目录中的\"login.info\"文件", Toast.LENGTH_LONG);
                    choicePath.openFileManager(GP.Directory_Selection);
                }
            });
            return;
        }
        toPathData toPathData = GP.to_pathList.get(position);
        holder.TV_accountName.setText(toPathData.fileName);
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GP.to_path = toPathData;
                choicePath.setText(toPathData.fileName);
                ToastUtils.toast(choicePath,"已选择"+toPathData.fileName);
                choicePath.finish();
            }
        });
        holder.rootView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog dialog = new AlertDialog.Builder(choicePath)
//                    .setIcon(R.mipmap.icon)//设置标题的图片
                        .setTitle("是否确定删除")//设置对话框的标题
                        .setMessage(toPathData.fileName+"路径")//设置对话框的内容
                        //设置对话框的按钮
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                GP.BR.add("取消");
                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @SuppressLint("NotifyDataSetChanged")
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                GP.BR.add("确定删除");
                                int move = GP.to_pathList.indexOf(toPathData);
                                GP.to_pathList.remove(toPathData);
                                choicePath.pathRecyclerViewAdapter.notifyDataSetChanged();
                                dialog.dismiss();
                            }
                        }).create();
                dialog.show();
                return true;
            }
        });
    }

    class AccountViewHoder extends RecyclerView.ViewHolder{
        TextView TV_accountName ;
        ConstraintLayout rootView;
        public AccountViewHoder(@NonNull View itemView) {
            super(itemView);
            TV_accountName = itemView.findViewById(R.id.TV_accountName);
            rootView = itemView.findViewById(R.id.account_list_item);
        }
    }
}
