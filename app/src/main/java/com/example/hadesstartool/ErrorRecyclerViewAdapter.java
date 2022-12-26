package com.example.hadesstartool;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;

import Utils.StringUtils;

public class ErrorRecyclerViewAdapter extends RecyclerView.Adapter<ErrorRecyclerViewAdapter.AccountViewHoder>{



    @NonNull
    @Override
    public AccountViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.account_list_item, parent, false);//解决宽度不能铺满

        AccountViewHoder accountViewHoder = new AccountViewHoder(view);
        return accountViewHoder;
    }

    @Override
    public int getItemCount() {
        return GP.errorIntList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull AccountViewHoder holder, int position) {
        int integer = GP.errorIntList.get(position);
        switch (integer){
            case GP.Insufficient_Permissions:
                holder.TV_accountName.setText("权限不足");
                holder.rootView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(GP.mainActivity.checkP()){
                            //重启Activity
                            GP.BR.add("已获权限,正在重启");
                            GP.mainActivity.reStart();
                        }else {
                            GP.mainActivity.checkPermission();
                        }
                    }
                });
                break;
            case GP.Game_Directory_Not_Exist:
                holder.TV_accountName.setText("游戏目录不存在");
                holder.rootView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        GP.mainActivity.choicePath();
                    }
                });
                break;
            case GP.No_Record:
                holder.TV_accountName.setText("无账号记录");
                break;
            default:
                holder.TV_accountName.setText("未知错误");
                holder.rootView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        GP.mainActivity.setBtn_about();
                    }
                });
                break;
        }

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
