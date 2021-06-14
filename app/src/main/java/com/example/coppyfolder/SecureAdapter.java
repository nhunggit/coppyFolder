package com.example.coppyfolder;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.crypto.NoSuchPaddingException;

public class SecureAdapter extends BaseAdapter {
        List<Path> list;
        Context context;
        Path path;
        process process;
        inAppActivity.callbackListener callbackListener;
        public ImageView imageButton;

    public SecureAdapter(List<Path> list, inAppActivity.callbackListener callbackListener) {
        this.list = list;
        this.callbackListener=callbackListener;
    }

    @Override
    public int getCount() {
        if(list!=null) {
            return list.size();
        }else
            return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            context=parent.getContext();
            LayoutInflater inflater= LayoutInflater.from(context);
            convertView=inflater.inflate(R.layout.item_view,parent,false);
        }

        path= list.get(position);
        imageButton= (ImageView)convertView.findViewById(R.id.image);
        Bitmap bitmap= BitmapFactory.decodeFile(path.getDecrypt());
        imageButton.setImageBitmap(bitmap);
        convertView.setClickable(false);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callbackListener.callback(position);
            }
        });
        return convertView;
    }

//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        context=parent.getContext();
//        LayoutInflater inflater= LayoutInflater.from(context);
//        View itemView= inflater.inflate(R.layout.item_view, parent, false);
//        ViewHolder viewHolder= new ViewHolder(itemView);
//        return viewHolder;
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//
//        path= list.get(position);
//        ImageButton imageButton= holder.imageButton;
//        TextView textView= holder.path;
//        ImageButton imageButton_menu= holder.imageButton_menu;
//
//        Bitmap bitmap= BitmapFactory.decodeFile(path.getDecrypt());
//        imageButton.setImageBitmap(bitmap);
//
////        Uri selectedImage = Uri.parse(path.getPathDecrypt());
////        try {
////            Bitmap bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(selectedImage));
////            imageButton.setImageBitmap(bitmap);
////        } catch (FileNotFoundException e) {
////            e.printStackTrace();
////        }
//
//        Log.d("nhungltk", "onMenuItemClick: "+path.getPathDecrypt());
//
//        textView.setText(path.getPathEncrypt());
//        imageButton_menu.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                PopupMenu popupMenu= new PopupMenu(context, v);
//                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                    @Override
//                    public boolean onMenuItemClick(MenuItem item) {
//                        if(item.getItemId()==(R.id.decrypt)){
//                            Path path_delete= list.get(position);
//                            try {
//                                process.decrypt(new FileInputStream(path_delete.getPathEncrypt()),new FileOutputStream(path_delete.getPathDecrypt()));
//                                MediaScannerConnection.scanFile(context, new String[]{path_delete.getPathDecrypt()}, null,
//                                        new MediaScannerConnection.OnScanCompletedListener() {
//                                            @Override
//                                            public void onScanCompleted(String path, Uri uri) {
//                                                Log.d("nhungltk", "onScanCompleted: ");
//                                            }
//                                        });
//                                list.remove(path_delete);
//                                Database.getInstance(context).deletePath(path_delete.getPathEncrypt(),path_delete.getPathDecrypt());
//                                process=new process();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            } catch (NoSuchAlgorithmException e) {
//                                e.printStackTrace();
//                            } catch (NoSuchPaddingException e) {
//                                e.printStackTrace();
//                            } catch (InvalidKeyException e) {
//                                e.printStackTrace();
//                            }
//                            notifyDataSetChanged();
//                        }
//                        return false;
//                    }
//                });
//                popupMenu.inflate(R.menu.menu);
//                popupMenu.show();
//            }
//        });
//        View view=holder.itemView;
//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d("nhungltk", "onClick: "+"ok");
//                callbackListener.callback(position);
//            }
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        if(list!=null) {
//            return list.size();
//        }else
//            return 0;
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder {
//        public ImageButton imageButton;
//        public TextView path;
//        public ImageButton imageButton_menu;
//
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//            imageButton= (ImageButton)itemView.findViewById(R.id.image);
//            path= (TextView)itemView.findViewById(R.id.path);
//            imageButton_menu= (ImageButton)itemView.findViewById(R.id.tuy_chon);
//        }
//    }
}
