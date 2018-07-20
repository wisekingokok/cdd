package com.chewuwuyou.app.transition_adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chewuwuyou.app.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xxy on 2016/10/20 0020.
 */

public class BancCardListAdapter extends RecyclerView.Adapter<BancCardListAdapter.MyViewHolder> {
    private Context context;
    private ItemClickListener itemClickListener;

    public BancCardListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_bank_card, parent, false));
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null)
                    itemClickListener.click(holder.text, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.text)
        TextView text;

        MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public interface ItemClickListener {
        void click(View v, int position);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
