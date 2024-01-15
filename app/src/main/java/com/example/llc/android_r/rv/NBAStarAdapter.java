package com.example.llc.android_r.rv;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.llc.android_r.R;

import java.util.List;

/**
 * com.example.llc.android_r.rv.NBAStarAdapter
 *
 * @author liulongchao
 * @since 2024/1/14
 */
public class NBAStarAdapter extends RecyclerView.Adapter<NBAStarAdapter.NBAStarHolder> {

    private Context context;
    private List<NBAStar> starList;

    public NBAStarAdapter(Context context, List<NBAStar> starList) {
        this.context = context;
        this.starList = starList;
    }

    public boolean isGroupHeader(int position) {
        if (position == 0) {
            return true;
        } else {
            String currentGroupName = getGroupName(position);
            String preGroupName = getGroupName(position - 1);
            if (TextUtils.equals(currentGroupName, preGroupName)) {
                return false;
            } else {
                return true;
            }
        }
    }

    public String getGroupName(int position) {
        return starList.get(position).getGroupName();
    }

    @NonNull
    @Override
    public NBAStarHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_item_star, null);
        return new NBAStarHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NBAStarHolder holder, int position) {
        holder.tv.setText(starList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return starList == null ? 0 : starList.size();
    }

    static class NBAStarHolder extends RecyclerView.ViewHolder{
        private TextView tv;
        public NBAStarHolder(@NonNull View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.tv_star);
        }
    }
}
