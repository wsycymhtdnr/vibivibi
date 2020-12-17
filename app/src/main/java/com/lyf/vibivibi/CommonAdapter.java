package com.lyf.vibivibi;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CommonAdapter extends RecyclerView.Adapter<CommonAdapter.VH> {
    private Context mContext;

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class VH extends RecyclerView.ViewHolder{
        public VH(View v) {
            super(v);
        }
    }

}
