package com.hustunique.jianguo.openkeychaindemo.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hustunique.jianguo.openkeychaindemo.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by JianGuo on 3/10/16.
 */
public class KeyAdapter extends RecyclerView.Adapter<KeyAdapter.KeyViewHolder> {
    private final LayoutInflater mLayoutInflater;
    private final Context mContext;
    private String[] mNames, mEmails, mCreations;
    private OnMyItemClickListener onMyItemClickListener;
    public interface OnMyItemClickListener {
        void onClick();
    }

    public KeyAdapter(Context context) {
        this.mContext = context;
        mNames = context.getResources().getStringArray(R.array.names);
        mEmails = context.getResources().getStringArray(R.array.emails);
        mCreations = context.getResources().getStringArray(R.array.creations);
        mLayoutInflater = LayoutInflater.from(context);
    }
    @Override
    public KeyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new KeyViewHolder(mLayoutInflater.inflate(R.layout.item_key, parent, false));
    }

    @Override
    public void onBindViewHolder(KeyViewHolder holder, int position) {
        holder.name.setText(mNames[position]);
        holder.email.setText(mEmails[position]);
        holder.creation.setText(mCreations[position]);
    }

    public void setOnItemListener(OnMyItemClickListener onItemListener) {
        this.onMyItemClickListener = onItemListener;
    }

    @Override
    public int getItemCount() {
        return mNames == null ? 0 : mNames.length;
    }

    public class KeyViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.key_list_item_name)
        TextView name;
        @Bind(R.id.key_list_item_email)
        TextView email;
        @Bind(R.id.key_list_item_creation)
        TextView creation;
        public KeyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.key_list_item_data)
        void onItemClick() {
            onMyItemClickListener.onClick();
        }
    }
}
