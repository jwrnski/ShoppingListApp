package com.example.shoppinglist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private final List<Item> mDataset;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textViewName;
        public TextView textViewAmount;
        public Button deleteButton;

        public ViewHolder(View view, final OnItemClickListener listener) {
            super(view);
            imageView = view.findViewById(R.id.imageViewItem);
            textViewName = view.findViewById(R.id.textViewItemName);
            textViewAmount = view.findViewById(R.id.textViewItemAmount);
            deleteButton = view.findViewById(R.id.deleteButton);
            deleteButton.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onDeleteClick(position);
                    }
                }
            });
        }
    }

    // Konstruktor klasy MyAdapter przyjmujący listę obiektów Item
    public MyAdapter(List<Item> myDataset) {
        mDataset = myDataset;
    }


    //
    @NonNull
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_layout, parent, false);
        return new ViewHolder(v, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.ViewHolder holder, int position) {
        Item item = mDataset.get(position);
        holder.textViewName.setText(item.getName());
        holder.textViewAmount.setText(String.valueOf(item.getAmount()));

        // Pobranie obrazków .png z folderu drawable
        int resourceId = holder.itemView.getContext().getResources().getIdentifier(
                item.getImage().replace(".png", "").toLowerCase(),
                "drawable",
                holder.itemView.getContext().getPackageName());

        if (resourceId != 0) {
            holder.imageView.setImageResource(resourceId);
        } else {
            holder.imageView.setImageResource(R.drawable.default_image);
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
