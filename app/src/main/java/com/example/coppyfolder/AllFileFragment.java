package com.example.coppyfolder;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AllFileFragment extends Fragment {

    public List<Path> list;
    SecureAdapter secureAdapter;
    process process;
    inAppActivity.callbackListener callbackListener;

    public AllFileFragment(SecureAdapter secureAdapter, inAppActivity.callbackListener callbackListener) {
        this.secureAdapter= secureAdapter;
        this.callbackListener= callbackListener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.all_file_fragment,container, false);
        RecyclerView recyclerView= (RecyclerView)view.findViewById(R.id.recycle_view);
        recyclerView.setAdapter(secureAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
