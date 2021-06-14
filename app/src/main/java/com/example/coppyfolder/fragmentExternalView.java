package com.example.coppyfolder;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;

public class fragmentExternalView extends Fragment {

    private int possistion;
    private List<Path> list;
    inAppActivity.callbackListener callbackListener;

    public fragmentExternalView(int possistion, List<Path>list) {
        this.possistion = possistion;
        this.list=list;
    }

    public int getPossistion() {
        return possistion;
    }

    public void setPossistion(int possistion) {
        this.possistion = possistion;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.external_file,container,false);
        ImageView imageView=(ImageView)view.findViewById(R.id.image_view);
        Path path=list.get(possistion);

        Bitmap bitmap= BitmapFactory.decodeFile(path.getDecrypt());
        imageView.setImageBitmap(bitmap);
        return view;
    }
}
