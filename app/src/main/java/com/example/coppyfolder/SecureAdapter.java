package com.example.coppyfolder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SecureAdapter extends RecyclerView.Adapter<SecureAdapter.ViewHolder> {
    List<String> list;

    public SecureAdapter(List<String> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context=parent.getContext();
        LayoutInflater inflater= LayoutInflater.from(context);
        View itemView= inflater.inflate(R.layout.item_view, parent, false);
        ViewHolder viewHolder= new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String path= list.get(position);
        ImageButton imageButton= holder.imageButton;
        TextView textView= holder.path;
       // imageButton.setImageDrawable();
        textView.setText(path);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ImageButton imageButton;
        public TextView path;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageButton= (ImageButton)itemView.findViewById(R.id.image);
            path= (TextView)itemView.findViewById(R.id.path);
        }
    }
}
