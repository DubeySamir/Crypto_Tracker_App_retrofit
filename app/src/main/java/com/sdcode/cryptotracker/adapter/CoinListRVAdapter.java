package com.sdcode.cryptotracker.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sdcode.cryptotracker.R;
import com.sdcode.cryptotracker.models.CoinListModelClass;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CoinListRVAdapter extends RecyclerView.Adapter<CoinListRVAdapter.CoinListViewHolder> {

    private final ArrayList<CoinListModelClass> coinListModelClass;

    private OnItemClickListener mListener;
    private Context context;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public String getCoinId(int position) {
        return coinListModelClass.get(position).getId();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public CoinListRVAdapter(ArrayList<CoinListModelClass> coinListModelClass) {
        this.coinListModelClass = coinListModelClass;
    }

    @NonNull
    @Override
    public CoinListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.rv_single_coin, parent, false);
        return new CoinListViewHolder(view, mListener);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CoinListViewHolder holder, int position) {
        CoinListModelClass modelClass = coinListModelClass.get(position);
        String imgURL = modelClass.getImage();
        Picasso.get().load(imgURL).into(holder.img);
        holder.tv_symbol.setText(modelClass.getSymbol());
        holder.tv_name.setText(modelClass.getName());
        holder.tv_current_price.setText(Double.toString(modelClass.getCurrent_price()));
    }

    @Override
    public int getItemCount() {
        return coinListModelClass.size();
    }

    public static class CoinListViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView tv_symbol, tv_name, tv_current_price;
        Context context;

        public CoinListViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            context = itemView.getContext();
            img = itemView.findViewById(R.id.img);
            tv_symbol = itemView.findViewById(R.id.tv_symbol);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_current_price = itemView.findViewById(R.id.tv_current_price);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null) {
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

}
